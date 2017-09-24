package pl.edu.agh.config;

import io.fd.vpp.jvpp.JVpp;
import io.fd.vpp.jvpp.JVppRegistry;
import io.fd.vpp.jvpp.JVppRegistryImpl;
import io.fd.vpp.jvpp.core.JVppCore;
import io.fd.vpp.jvpp.core.JVppCoreImpl;
import io.fd.vpp.jvpp.core.future.FutureJVppCoreFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ComponentScan({"pl.edu.agh"})
public class AppConfig {
    @Bean("registry")
    public JVppRegistry jVppRegistry(JVpp jVpp) throws IOException {
        JVppRegistry registry =  new JVppRegistryImpl("Test");
        //registry.register(jVpp, new MyCallback());

        return registry;
    }

    @Bean
    public JVppCore jVpp() throws IOException {
        return new JVppCoreImpl();
    }

    @Bean
    public FutureJVppCoreFacade futureJVppCoreFacade(JVppRegistry registry, JVppCore core) throws IOException {
        return new FutureJVppCoreFacade(registry, core);
    }
}
