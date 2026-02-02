package com.ghostapidetector.Ghost.API.detector.rules;

import com.ghostapidetector.Ghost.API.detector.model.APIinfo;

public interface ApiRule {
    void apply(APIinfo api);
}
