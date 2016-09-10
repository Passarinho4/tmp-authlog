package com.tegess.config.jackson

import java.util.Collections

import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import com.avsystem.commons._
import com.avsystem.commons.jiop.JavaInterop._
import org.springframework.http.converter.HttpMessageConverter


@Configuration
class JacksonConfig {

  @Bean
  def mappingJackson2HttpMessageConverter:MappingJackson2HttpMessageConverter = {
    new MappingJackson2HttpMessageConverter(new ScalaObjectMapper)
  }
  @Bean
  def restTemplate: RestTemplate = {
    new RestTemplate().setup(_.setMessageConverters(Collections.singleton[HttpMessageConverter[_]](mappingJackson2HttpMessageConverter).asScala.toList.asJava))
  }

}
