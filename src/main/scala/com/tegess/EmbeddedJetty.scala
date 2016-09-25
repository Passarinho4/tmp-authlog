package com.tegess

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.{EnableAutoConfiguration, SpringBootApplication}
import org.springframework.context.annotation.{ComponentScan, Configuration}
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

@SpringBootApplication
@ComponentScan(Array("com.tegess"))
@Configuration
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(prePostEnabled=true)
class SampleConfig

object EmbeddedJetty extends App {
  SpringApplication.run(classOf[SampleConfig])
}
