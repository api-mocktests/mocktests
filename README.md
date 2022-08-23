# MockTests

## Descrição

Este projeto é uma biblioteca Java construída com Gradle que encapsula lógica e dependências para oferecer automatizações no desenvolvimento de testes de integração para aplicações back-end desenvolvidas com Java + Spring Boot.

## :hammer: Dependências

- org.springframework.boot:spring-boot-starter
- org.springframework.boot:spring-boot-starter-web
- org.springframework.boot:spring-boot-starter-test

## Repositório publicado

Disponivel no Jitpack: 
https://jitpack.io/#api-mocktests/mocktests/1.7.5

### Utilizando em projetos Gradle: 

Adicione esta linha em seus projetos no ```build.gradle```

```
repositories {
	//...
	maven { url 'https://jitpack.io' }
}
```

E adicione essa implementação em sus dependências:

```
dependencies {
	 implementation 'com.github.api-mocktests:mocktests:1.7.5'
}
```

### Utilizando em projetos Maven:

Adicione esse código no seu ```pom.xml```

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

E adicione essa implementação nas dependências:

```
<dependency>
	    <groupId>com.github.api-mocktests</groupId>
	    <artifactId>mocktests</artifactId>
	    <version>1.7.5</version>
</dependency>
```
