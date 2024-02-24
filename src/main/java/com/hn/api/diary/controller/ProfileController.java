package com.hn.api.diary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final Environment env;

    // deploy.sh,
    // 포트를 사용하는지 체크해서 진행하면 간단한데,
    // 이 메서드를 이용하는 게 어떤 이점이 있는지 고민해 볼 것.
    @GetMapping("/profile")
    public String getProfile(){
        List<String> profile = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("set1", "set2");
        String defaultProfile = profile.isEmpty() ? "default" : profile.get(0);

        return profile.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }
}
