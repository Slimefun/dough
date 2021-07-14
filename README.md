# :bagel: Dough

<hr />
<p align="center">
    <a href="https://github.com/baked-libs/dough/actions">
        <img alt="Build Status" src="https://github.com/baked-libs/dough/actions/workflows/maven.yml/badge.svg?event=push" />
    </a>
    <a href="https://search.maven.org/search?q=baked-libs">
        <img alt="Maven Central" src="https://img.shields.io/maven-central/v/io.github.baked-libs/dough?color=1074ad&logo=apache-maven" />
    </a>
    <a href="https://sonarcloud.io/project/overview?id=baked-libs_dough">
        <img alt="Code Coverage" src="https://sonarcloud.io/api/project_badges/measure?project=baked-libs_dough&metric=coverage" />
    </a>
    <a href="https://sonarcloud.io/project/overview?id=baked-libs_dough">
        <img alt="Maintainability" src="https://sonarcloud.io/api/project_badges/measure?project=baked-libs_dough&metric=sqale_rating" />
    </a>
</p>
<hr />

Formerly known as "cs-corelib2", dough is a very powerful library aiming to help the everyday Spigot/Plugin developer.<br>
It is packed to the brim with useful features and APIs to use and play around with.

**Note that this project is still under heavy development, we don't advise using this just yet as some things are still drafts and subject to change. Documentation is still lacking and we try to improve this until we go live. We plan to go live in September of 2021. Check back then! ;)**

## :page_facing_up: Table of contents
1. [Getting Started](#mag-getting-started)
    - [Adding dough via gradle](#adding-dough-via-gradle)
    - [Adding dough via Maven](#adding-dough-via-maven)
2. [Features & Documentation](#sparkles-features-and-documentation)
    - TODO
3. [Discord server](#headphones-discord-server)

## :mag: Getting Started
Dough is hosted on maven-central (OSS Sonatype) for easy access.
Furthermore it consists of multiple different submodules. You will
learn about the different modules in a later section ([:sparkles: Features and Documentation](#sparkles-features-and-documentation)).

If you want to utilise the entirety of dough, use the artifact `dough-api`.<br>
Otherwise replace `dough-api` in the following examples with whatever module you want to import. Note that
some modules have dependencies on other modules, all modules require `dough-common` as an example.

### Adding dough via gradle
Dough can easily be included in gradle using mavenCentral.<br />
Simply replace `[DOUGH VERSION]` with the most up to date version of dough:
![Maven Central](https://img.shields.io/maven-central/v/io.github.baked-libs/dough?label=latest%20version)

```gradle
repositories {
	mavenCentral()
}

dependencies {
	compileOnly 'io.github.baked-libs:dough-api:[DOUGH VERSION]'
}
```

To shadow dough and relocate it:
```gradle
plugins {
  id "com.github.johnrengelman.shadow" version "6.1.0"
}

shadowJar {
   relocate "io.github.bakedlibs.dough", "[YOUR PACKAGE].dough"
}
```

### Adding dough via Maven
Dough can easily be included be added using maven-central.<br />
Simply replace `[DOUGH VERSION]` with the most up to date version of dough:
![Maven Central](https://img.shields.io/maven-central/v/io.github.baked-libs/dough?label=latest%20version)

```xml
<dependencies>
  <dependency>
    <groupId>io.github.baked-libs</groupId>
    <artifactId>dough-api</artifactId>
    <version>[DOUGH VERSION]</version>
    <scope>compile</scope>
  </dependency>
</dependencies>
```

To shadow dough and relocate it:
```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-shade-plugin</artifactId>
      <version>3.2.1</version>

      <configuration>
        <relocations>
          <relocation>
            <pattern>io.github.bakedlibs.dough</pattern>
            <shadedPattern>[YOUR PACKAGE].dough</shadedPattern>
          </relocation>
        </relocations>
      </configuration>

      <executions>
        <execution>
          <phase>package</phase>
          <goals>
            <goal>shade</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

## :sparkles: Features and Documentation
**TODO: Finish this section**

## :headphones: Discord server
You can also find us on discord by the way!
If you need any help with dough or have a question regarding this project, feel free to join and connect with other members of the community.
Note that this server is not as active at times, so response times may vary.

<p align="center">
  <a href="https://discord.gg/c8tk8rP8Wb">
    <img src="https://discordapp.com/api/guilds/862336191839600650/widget.png?style=banner3" alt="Discord Invite"/>
  </a>
</p>
