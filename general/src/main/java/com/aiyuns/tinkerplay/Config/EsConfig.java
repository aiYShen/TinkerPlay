package com.aiyuns.tinkerplay.Config;

import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao.Repository.EsUserRepository;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.client.erhlc.RestClients;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactory;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * @Author: aiYunS @Date: 2023年9月12日, 0012 下午 3:38:29 @Description: Elasticsearch配置类
 */
@Configuration
@EnableJpaRepositories("com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao.Repository")
public class EsConfig {

  @Value("${elasticsearch.rest.uris}")
  private String uris;

  @Value("${elasticsearch.rest.port}")
  private String port;

  @Bean(name = "myElasticsearchClient")
  public RestHighLevelClient myElasticsearchClient() {
    if (uris.startsWith("http://")) {
      uris = uris.replace("http://", "");
    }
    final ClientConfiguration clientConfiguration =
        ClientConfiguration.builder()
            // 指定Elasticsearch服务器的主机和端口
            .connectedTo(uris + ":" + port)
            .build();
    return RestClients.create(clientConfiguration).rest();
  }

  @Bean("elasticsearchTemplate")
  public ElasticsearchRestTemplate elasticsearchTemplate() {
    ElasticsearchRestTemplate elasticsearchRestTemplate =
        new ElasticsearchRestTemplate(myElasticsearchClient());
    return elasticsearchRestTemplate;
  }

  @Bean("myEsUserRepository")
  public EsUserRepository myEsUserRepository() {
    RepositoryFactorySupport factory = new ElasticsearchRepositoryFactory(elasticsearchTemplate());
    return factory.getRepository(EsUserRepository.class);
  }

  @Bean
  public RestClientTransport restClientTransport(
      RestClient restClient,
      JsonpMapper jsonMapper,
      ObjectProvider<RestClientOptions> restClientOptions) {
    return new RestClientTransport(restClient, jsonMapper, restClientOptions.getIfAvailable());
  }
}
