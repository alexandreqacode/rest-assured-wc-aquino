package br.restassured;

import io.restassured.RestAssured;
import org.junit.Test;

import java.io.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public class FileTest {

    @Test
    public void deveObrigarEnvioArquivo(){

        given()
                .log().all()
        .when()
                .post("https://restapi.wcaquino.me/upload")
        .then()
                .log().all()
                .statusCode(404)
                .body("error", is("Arquivo não enviado"))
        ;

    }

    @Test
    public void deveFazerUploadArquivo(){

        given()
                .log().all()
                .multiPart("arquivo", new File("src/main/resources/apiEnviaArquivo.pdf"))
        .when()
                .post("https://restapi.wcaquino.me/upload")
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("apiEnviaArquivo.pdf"))
        ;

    }

    @Test
    public void naodeveFazerUploadArquivoGrande(){

        given()
                .log().all()
                .multiPart("arquivo", new File("src/main/resources/cats.jpg"))
        .when()
                .post("https://restapi.wcaquino.me/upload")
        .then()
                .log().all()
                .time(lessThan(5000L))
                .statusCode(413)
        ;

    }

    @Test
    public void deveBaixarArquivo() throws IOException {

        byte[] image = given()
                .log().all()
        .when()
                .get("https://restapi.wcaquino.me/download")
        .then()
                .log().all()
                .statusCode(200)
                .extract().asByteArray()
        ;

        File imagem = new File("src/main/resources/file.jpg");
        OutputStream out = new FileOutputStream(imagem);
        out.write(image);
        out.close();

        System.out.println(imagem.length());

    }
}
