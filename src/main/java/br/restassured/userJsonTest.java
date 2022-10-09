package br.restassured;

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class userJsonTest {

    @BeforeClass
    public static void setup(){

        RestAssured.baseURI = "https://restapi.wcaquino.me";
        RestAssured.port = 443;
        RestAssured.basePath = "";
    }

    @Test
    public void deveVerificarPrimeiroNivel(){

        given()
        .when()
                .get("/users/1")
        .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", containsString("Silva"))
                .body("age", greaterThan(28))
        ;
    }

    @Test
    public void deveVerificarSegundoNivel(){

        given()
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .body("name", containsString("Joaquina"))
                .body("endereco.rua", is("Rua dos bobos"))
        ;

    }

    @Test
    public void deveVerificarLista(){

        given()
                .when()
                .get("/users/3")
                .then()
                .statusCode(200)
                .body("name", containsString("Ana"))
                .body("filhos", hasSize(2))
                .body("filhos[0].name", is("Zezinho"))
                .body("filhos[1].name", is("Luizinho"))
                .body("filhos.name", hasItem("Zezinho"))
                .body("filhos.name", hasItems("Zezinho", "Luizinho"))
        ;

    }

    @Test
    public void deveRetornarErroUsuarioInexistente(){

        given()
        .when()
                .get("/users/4")
        .then()
                .statusCode(404)
                .body("error", is("Usuário inexistente"))

        ;

    }

    @Test
    public void deveVerificarListaRaiz(){

        given()
        .when()
                .get("/users")
        .then()
                .statusCode(200)
                .body("", hasSize(3))
                .body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
                .body("age[1]", is(25))
                .body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
                .body("salary", contains(1234.5678f, 2500, null))
        ;

    }

    @Test
    public void deveFazerVerificacoesAvancadas(){

        given()
        .when()
                .get("/users")
        .then()
                .statusCode(200)
                .body("", hasSize(3))
                .body("age.findAll{it <= 25 && it > 20}.size()", is(1 ))
                .body("findAll{it.age <=25 && it.age > 20}.name", hasItem("Maria Joaquina"))
                .body("findAll{it.age <=25}[0].name", is("Maria Joaquina"))
                .body("findAll{it.age <=25}[-1].name", is("Ana Júlia"))
                .body("find{it.age <= 25}.name", is("Maria Joaquina"))
                .body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
                .body("findAll{it.name.length() > 10}.name", hasItems("João da Silva", "Maria Joaquina"))
                .body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
                .body("age.collect{it * 2}", hasItems(60, 50, 40))
                .body("id.max()", is(3))
                .body("salary.min()", is(1234.5678f))
        ;
    }
}
