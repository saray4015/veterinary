package co.edu.umanizales.veterinary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
public class VeterinaryApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(VeterinaryApplication.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path", "/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("No se pudo determinar la dirección IP; usando localhost como fallback.");
        }

        log.info("""
                
                ----------------------------------------------------------
                	Aplicación '{}' está ejecutándose! URLs de acceso:
                	Local: 		{}://localhost:{}{}
                	Externo: 	{}://{}:{}{}
                	Perfil(es): 	{}
                ----------------------------------------------------------
                """,
                env.getProperty("spring.application.name", "Veterinary"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles()
        );
    }
}
