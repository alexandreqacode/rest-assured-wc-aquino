package br.restassured;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class verbosTeste {

    @Test
    public void deveSalvarUsuario(){

        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\": \"Jose\", \"age\": 50}")
        .when()
                .post("https://restapi.wcaquino.me/users")
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Jose"))
                .body("age", is(50))
        ;

    }

    @Test
    public void naoDeveSalvarUsuarioSemNome(){

        given()
                .log().all()
                .contentType("application/json")
                .body("{\"age\": 50}")
        .when()
                .post("https://restapi.wcaquino.me/users")
        .then()
                .log().all()
                .statusCode(400)
                .body("id", is(nullValue()))
                .body("error", is("Name é um atributo obrigatório"))
        ;

    }

    @Test
    public void deveSalvarUsuarioViaXML(){

        given()
                .log().all()
                .contentType(ContentType.XML)
                .body("<user><name>Jose</name><age>50</age></user>")
        .when()
                .post("https://restapi.wcaquino.me/usersXML")
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("user.name", is("Jose"))
                .body("user.age", is("50"))
        ;

    }

    @Test
    public void deveAlterarUsuario(){

        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\": \"Usuario Alterado\", \"age\": 5000}")
        .when()
                .put("https://restapi.wcaquino.me/users/1")
        .then()
                .log().all()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("name", is("Usuario Alterado"))
                .body("age", is(5000))
        ;

    }

    @Test
    public void devoCustomizarURL(){

        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\": \"Usuario Alterado\", \"age\": 5000}")
                .pathParam("entidade", "users")
                .pathParam("userId", 1)
        .when()
                .put("https://restapi.wcaquino.me/{entidade}/{userId}")
        .then()
                .log().all()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("name", is("Usuario Alterado"))
                .body("age", is(5000))
        ;

    }

    @Test
    public void deveRemoverUsuario(){

        given()
                .log().all()
        .when()
                .delete("https://restapi.wcaquino.me/users/1")
        .then()
                .log().all()
                .statusCode(204)
        ;

    }

    @Test
    public void deveRemoverUsuarioInexistente(){

        given()
                .log().all()
        .when()
                .delete("https://restapi.wcaquino.me/users/100")
        .then()
                .log().all()
                .statusCode(400)
                .body("error", is("Registro inexistente"))
        ;

    }

    @Test
    public void deveSalvarUsuarioUsandoMap(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Usuario via Map");
        params.put("age", 25);

        given()
                .log().all()
                .contentType("application/json")
                .body(params)
        .when()
                .post("https://restapi.wcaquino.me/users")
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Usuario via Map"))
                .body("age", is(25))
        ;

    }

    @Test
    public void deveSalvarUsuarioUsandoObjeto(){
        User user = new User("Usuario via objeto", 35);

        given()
                .log().all()
                .contentType("application/json")
                .body(user)
                .when()
                .post("https://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Usuario via objeto"))
                .body("age", is(35))
        ;

    }

    @Test
    public void deveDeserializarObjetoAoSalvarUsuarioUsando(){
        User user = new User("Usuario Deserializado", 35);

        User usuarioInserido = given()
                .log().all()
                .contentType("application/json")
                .body(user)
        .when()
                .post("https://restapi.wcaquino.me/users")
        .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(User.class)
        ;
        System.out.println(usuarioInserido);
        Assert.assertThat(usuarioInserido.getId(), notNullValue());
        Assert.assertEquals("Usuario Deserializado", usuarioInserido.getName());
        Assert.assertThat(usuarioInserido.getAge(), is(35));

    }
}

