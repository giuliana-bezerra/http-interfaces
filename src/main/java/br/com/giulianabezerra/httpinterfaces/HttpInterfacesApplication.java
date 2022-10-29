package br.com.giulianabezerra.httpinterfaces;

import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootApplication
public class HttpInterfacesApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpInterfacesApplication.class, args);
	}

	@Bean
	HttpServiceProxyFactory httpServiceProxyFactory1(
			WebClient.Builder clientBuilder) {
		return WebClientAdapter
				.createHttpServiceProxyFactory(clientBuilder
						.baseUrl("https://my-json-server.typicode.com/typicode/demo"));
	}

	@Bean
	PostClient postClient(HttpServiceProxyFactory factory) {
		return factory.createClient(PostClient.class);
	}

	@HttpExchange("/posts")
	interface PostClient {
		@GetExchange
		List<Post> list();

		@GetExchange("/{id}")
		Post get(@PathVariable("id") Long id);

		@PostExchange
		Post create(@RequestBody Post post);
	}

	record Post(Long id, String title) {

	}

	@Bean
	ApplicationRunner applicationRunner(PostClient postClient) {
		return args -> {
			System.out.println("List posts: " + postClient.list());
			System.out.println("Post 1: " + postClient.get(1L));
			System.out.println("New post: "
					+ postClient.create(new Post(4L, "Post 4")));
		};
	}
}
