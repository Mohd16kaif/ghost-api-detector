package com.ghostapidetector.Ghost.API.detector.discovery;

import com.ghostapidetector.Ghost.API.detector.model.DiscoveredApi;
import org.objectweb.asm.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.ASM9;

public class SpringMvcApiDiscovery {

    public List<DiscoveredApi> discover(InputStream classStream) throws Exception {

        System.out.println(">>> scanning class with ASM");

        List<DiscoveredApi> apis = new ArrayList<>();

        ClassReader reader = new ClassReader(classStream);
        reader.accept(new ClassVisitor(ASM9) {

            private String className;
            private String basePath = null;
            private boolean isController = false;
            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

                // Detect controller
                if (desc.equals("Lorg/springframework/web/bind/annotation/RestController;")
                        || desc.equals("Lorg/springframework/stereotype/Controller;")) {
                    isController = true;
                }

                // Detect class-level @RequestMapping
                if (desc.equals("Lorg/springframework/web/bind/annotation/RequestMapping;")) {
                    return new AnnotationVisitor(ASM9) {
                        @Override
                        public void visit(String name, Object value) {
                            if ("value".equals(name)) {
                                basePath = value.toString();
                            }
                        }
                    };
                }

                return super.visitAnnotation(desc, visible);
            }


            @Override
            public void visit(
                    int version, int access, String name,
                    String signature, String superName, String[] interfaces) {

                this.className = name.replace("/", ".");
            }


            @Override
            public MethodVisitor visitMethod(
                    int access, String methodName, String descriptor,
                    String signature, String[] exceptions) {

                if (!isController) return null;

                return new MethodVisitor(ASM9) {

                    private String methodPath = "";
                    private String httpMethod = null;

                    @Override
                    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

                        // Detect HTTP method
                        if (desc.equals("Lorg/springframework/web/bind/annotation/GetMapping;"))
                            httpMethod = "GET";
                        else if (desc.equals("Lorg/springframework/web/bind/annotation/PostMapping;"))
                            httpMethod = "POST";
                        else if (desc.equals("Lorg/springframework/web/bind/annotation/PutMapping;"))
                            httpMethod = "PUT";
                        else if (desc.equals("Lorg/springframework/web/bind/annotation/DeleteMapping;"))
                            httpMethod = "DELETE";

                        if (httpMethod == null) {
                            return super.visitAnnotation(desc, visible);
                        }

                        // Capture method-level path
                        return new AnnotationVisitor(ASM9) {
                            @Override
                            public void visit(String name, Object value) {
                                if ("value".equals(name)) {
                                    if (value instanceof String[]) {
                                        methodPath = ((String[]) value)[0];
                                    } else {
                                        methodPath = value.toString();
                                    }
                                }
                            }



                            @Override
                            public void visitEnd() {
                                DiscoveredApi api = new DiscoveredApi();
                                api.className = className;
                                api.methodName = methodName;
                                api.httpMethod = httpMethod;

                                String fullPath =
                                        (basePath != null ? basePath : "") +
                                                (methodPath != null ? methodPath : "");

                                api.path = fullPath.isEmpty() ? "/" : fullPath;

                                boolean exists = apis.stream().anyMatch(a ->
                                        a.className.equals(api.className) &&
                                                a.methodName.equals(api.methodName) &&
                                                a.httpMethod.equals(api.httpMethod) &&
                                                a.path.equals(api.path)
                                );

                                if (!exists) {
                                    apis.add(api);
                                }


                                System.out.println(
                                        api.className + " " +
                                                api.httpMethod + " " +
                                                api.path
                                );
                            }
                        };
                    }
                };
            }

            }, 0);

        return apis;
    }
}
