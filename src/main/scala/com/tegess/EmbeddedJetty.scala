package com.tegess

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.{EnableAutoConfiguration, SpringBootApplication}
import org.springframework.context.annotation.{ComponentScan, Configuration}

@SpringBootApplication
@ComponentScan(Array("com.tegess"))
@Configuration
@EnableAutoConfiguration
class SampleConfig

object EmbeddedJetty extends App {
  SpringApplication.run(classOf[SampleConfig])
}
