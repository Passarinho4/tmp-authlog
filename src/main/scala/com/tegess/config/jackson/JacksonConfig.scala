package com.tegess.config.jackson

import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
class JacksonConfig {

  @Bean
  def mappingJackson2HttpMessageConverter:MappingJackson2HttpMessageConverter = {
    new MappingJackson2HttpMessageConverter(new ScalaObjectMapper)
  }

}
