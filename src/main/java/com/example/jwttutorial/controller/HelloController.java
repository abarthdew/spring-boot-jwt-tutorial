package com.example.jwttutorial.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class HelloController {

  @GetMapping("/hello_test")
  public ResponseEntity<String> hello() {
    log.debug("hello world!_1");
    return ResponseEntity.ok("hello world!_2");
  }

}
