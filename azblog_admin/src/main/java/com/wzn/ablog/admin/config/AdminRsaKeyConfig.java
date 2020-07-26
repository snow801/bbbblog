package com.wzn.ablog.admin.config;

import com.wzn.ablog.common.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;


@ConfigurationProperties(prefix = "rsakey")
@PropertySource("classpath:/application.yml")
@Data
public class AdminRsaKeyConfig {
    private String publicKeyName;
    private String privateKeyName;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    public void createRsa() throws Exception {
        this.privateKey = RsaUtils.getPrivateKey(privateKeyName);
        this.publicKey = RsaUtils.getPublicKey(publicKeyName);
    }
}