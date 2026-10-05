package apresentacao;

import java.util.UUID;

import io.javalin.Javalin;
import persistencia.UrlMongoDAO;
import persistencia.UrlRedisDAO;

public class Main {
    public static void main(String[] args) {

        var app = Javalin.create(config -> {
            config.routes.get("/", ctx -> {
                ctx.html("<html>" +
                        "<head>" +
                        "    <meta charset='UTF-8'>" +
                        "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "    <title>Encurtador de URLs</title>" +
                        "    <style>" +
                        "        * { box-sizing: border-box; }" +
                        "        body {" +
                        "            margin: 0;" +
                        "            font-family: Arial, sans-serif;" +
                        "            background: linear-gradient(135deg, #667eea, #764ba2);" +
                        "            min-height: 100vh;" +
                        "            display: flex;" +
                        "            justify-content: center;" +
                        "            align-items: center;" +
                        "        }" +
                        "        .card {" +
                        "            background: white;" +
                        "            width: 90%;" +
                        "            max-width: 550px;" +
                        "            padding: 40px;" +
                        "            border-radius: 16px;" +
                        "            box-shadow: 0 15px 40px rgba(0,0,0,0.25);" +
                        "            text-align: center;" +
                        "        }" +
                        "        h1 {" +
                        "            margin: 0 0 10px 0;" +
                        "            color: #333;" +
                        "            font-size: 30px;" +
                        "        }" +
                        "        p {" +
                        "            color: #777;" +
                        "            margin-bottom: 30px;" +
                        "        }" +
                        "        label {" +
                        "            display: block;" +
                        "            text-align: left;" +
                        "            margin-bottom: 8px;" +
                        "            font-weight: bold;" +
                        "            color: #444;" +
                        "        }" +
                        "        input[type='text'] {" +
                        "            width: 100%;" +
                        "            padding: 14px;" +
                        "            border: 2px solid #ddd;" +
                        "            border-radius: 8px;" +
                        "            font-size: 16px;" +
                        "            outline: none;" +
                        "            transition: 0.2s;" +
                        "        }" +
                        "        input[type='text']:focus {" +
                        "            border-color: #667eea;" +
                        "        }" +
                        "        input[type='submit'] {" +
                        "            width: 100%;" +
                        "            margin-top: 20px;" +
                        "            padding: 14px;" +
                        "            border: none;" +
                        "            border-radius: 8px;" +
                        "            background: #667eea;" +
                        "            color: white;" +
                        "            font-size: 17px;" +
                        "            font-weight: bold;" +
                        "            cursor: pointer;" +
                        "            transition: 0.2s;" +
                        "        }" +
                        "        input[type='submit']:hover {" +
                        "            background: #5568d8;" +
                        "            transform: translateY(-1px);" +
                        "        }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class='card'>" +
                        "        <h1>🔗 Encurtador de URLs</h1>" +
                        "        <p>Transforme URLs longas em links curtos e fáceis de compartilhar.</p>" +
                        "        <form action='/encurtar' method='post'>" +
                        "            <label for='url'>Digite a URL</label>" +
                        "            <input type='text' id='url' name='url' " +
                        "                   placeholder='https://exemplo.com/uma-url-muito-grande' required>" +
                        "            <input type='submit' value='Encurtar URL'>" +
                        "        </form>" +
                        "    </div>" +
                        "</body>" +
                        "</html>");
            });

            config.routes.post("/encurtar", ctx -> {
                String url = ctx.formParam("url");
                if (url != null) {
                    String urlEncurtada = UUID.randomUUID().toString().split("-")[0];
                    urlEncurtada = new UrlMongoDAO().encurtar(url, urlEncurtada);
                    new UrlRedisDAO().salvarNoCache(url, urlEncurtada);
                    ctx.html("<html>" +
                            "<head>" +
                            "    <meta charset='UTF-8'>" +
                            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                            "    <title>URL Encurtada</title>" +
                            "    <style>" +
                            "        * { box-sizing: border-box; }" +
                            "        body {" +
                            "            margin: 0;" +
                            "            font-family: Arial, sans-serif;" +
                            "            background: linear-gradient(135deg, #667eea, #764ba2);" +
                            "            min-height: 100vh;" +
                            "            display: flex;" +
                            "            justify-content: center;" +
                            "            align-items: center;" +
                            "        }" +
                            "        .card {" +
                            "            background: white;" +
                            "            width: 90%;" +
                            "            max-width: 600px;" +
                            "            padding: 40px;" +
                            "            border-radius: 16px;" +
                            "            box-shadow: 0 15px 40px rgba(0,0,0,0.25);" +
                            "        }" +
                            "        h1 {" +
                            "            text-align: center;" +
                            "            color: #333;" +
                            "            margin-top: 0;" +
                            "        }" +
                            "        .original {" +
                            "            background: #f5f5f5;" +
                            "            padding: 15px;" +
                            "            border-radius: 8px;" +
                            "            word-break: break-all;" +
                            "            color: #666;" +
                            "            margin-bottom: 20px;" +
                            "        }" +
                            "        .resultado {" +
                            "            background: #eef1ff;" +
                            "            border: 2px solid #667eea;" +
                            "            padding: 20px;" +
                            "            border-radius: 10px;" +
                            "            text-align: center;" +
                            "        }" +
                            "        .resultado label {" +
                            "            display: block;" +
                            "            color: #555;" +
                            "            font-weight: bold;" +
                            "            margin-bottom: 10px;" +
                            "        }" +
                            "        .resultado a {" +
                            "            color: #667eea;" +
                            "            font-size: 20px;" +
                            "            font-weight: bold;" +
                            "            text-decoration: none;" +
                            "            word-break: break-all;" +
                            "        }" +
                            "        .resultado a:hover {" +
                            "            text-decoration: underline;" +
                            "        }" +
                            "        .voltar {" +
                            "            display: block;" +
                            "            text-align: center;" +
                            "            margin-top: 25px;" +
                            "            color: #667eea;" +
                            "            text-decoration: none;" +
                            "        }" +
                            "    </style>" +
                            "</head>" +
                            "<body>" +
                            "    <div class='card'>" +
                            "        <h1>🔗 URL Encurtada</h1>" +
                            "        <div class='original'>" +
                            "            <strong>URL original:</strong><br>" +
                            "            " + url +
                            "        </div>" +
                            "        <div class='resultado'>" +
                            "            <label>Sua nova URL:</label>" +
                            "            <a href='" + urlEncurtada + "' target='_blank'>" +
                            "                " + urlEncurtada +
                            "            </a>" +
                            "        </div>" +
                            "        <a class='voltar' href='/'>← Encurtar outra URL</a>" +
                            "    </div>" +
                            "</body>" +
                            "</html>");

                } else {
                    ctx.html("<html>" +
                            "<head>" +
                            "    <meta charset='UTF-8'>" +
                            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                            "    <title>Erro</title>" +
                            "    <style>" +
                            "        * { box-sizing: border-box; }" +
                            "        body {" +
                            "            margin: 0;" +
                            "            font-family: Arial, sans-serif;" +
                            "            background: linear-gradient(135deg, #667eea, #764ba2);" +
                            "            min-height: 100vh;" +
                            "            display: flex;" +
                            "            justify-content: center;" +
                            "            align-items: center;" +
                            "        }" +
                            "        .card {" +
                            "            background: white;" +
                            "            width: 90%;" +
                            "            max-width: 500px;" +
                            "            padding: 40px;" +
                            "            border-radius: 16px;" +
                            "            box-shadow: 0 15px 40px rgba(0,0,0,0.25);" +
                            "            text-align: center;" +
                            "        }" +
                            "        .icone {" +
                            "            font-size: 50px;" +
                            "            margin-bottom: 15px;" +
                            "        }" +
                            "        h1 {" +
                            "            color: #d9534f;" +
                            "            margin: 0 0 15px 0;" +
                            "        }" +
                            "        p {" +
                            "            color: #666;" +
                            "            font-size: 17px;" +
                            "            margin-bottom: 25px;" +
                            "        }" +
                            "        .voltar {" +
                            "            display: inline-block;" +
                            "            padding: 12px 22px;" +
                            "            background: #667eea;" +
                            "            color: white;" +
                            "            border-radius: 8px;" +
                            "            text-decoration: none;" +
                            "            font-weight: bold;" +
                            "        }" +
                            "        .voltar:hover {" +
                            "            background: #5568d8;" +
                            "        }" +
                            "    </style>" +
                            "</head>" +
                            "<body>" +
                            "    <div class='card'>" +
                            "        <div class='icone'>⚠️</div>" +
                            "        <h1>URL inválida</h1>" +
                            "        <p>A URL informada está em branco ou não é válida.</p>" +
                            "        <a class='voltar' href='/'>← Tentar novamente</a>" +
                            "    </div>" +
                            "</body>" +
                            "</html>");
                }

            });

            config.routes.get("/{urlEncurtada}", ctx -> {
                String url = null;
                String urlEncurtada = ctx.pathParam("urlEncurtada");
                if (urlEncurtada != null) {
                    url = new UrlRedisDAO().obter(urlEncurtada);
                    if (url == null) {
                        System.out.println("Buscando via MongoDB");
                        url = new UrlMongoDAO().obter(urlEncurtada);
                        new UrlRedisDAO().salvarNoCache(url, urlEncurtada);
                    } else {
                        System.out.println("Buscando via Redis");

                    }
                    if (url != null) {
                        ctx.redirect(url);
                    } else {
                        ctx.html("<html>" +
                                "<head>" +
                                "    <meta charset='UTF-8'>" +
                                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                                "    <title>Erro</title>" +
                                "    <style>" +
                                "        * { box-sizing: border-box; }" +
                                "        body {" +
                                "            margin: 0;" +
                                "            font-family: Arial, sans-serif;" +
                                "            background: linear-gradient(135deg, #667eea, #764ba2);" +
                                "            min-height: 100vh;" +
                                "            display: flex;" +
                                "            justify-content: center;" +
                                "            align-items: center;" +
                                "        }" +
                                "        .card {" +
                                "            background: white;" +
                                "            width: 90%;" +
                                "            max-width: 500px;" +
                                "            padding: 40px;" +
                                "            border-radius: 16px;" +
                                "            box-shadow: 0 15px 40px rgba(0,0,0,0.25);" +
                                "            text-align: center;" +
                                "        }" +
                                "        .icone {" +
                                "            font-size: 50px;" +
                                "            margin-bottom: 15px;" +
                                "        }" +
                                "        h1 {" +
                                "            color: #d9534f;" +
                                "            margin: 0 0 15px 0;" +
                                "        }" +
                                "        p {" +
                                "            color: #666;" +
                                "            font-size: 17px;" +
                                "            margin-bottom: 25px;" +
                                "        }" +
                                "        .voltar {" +
                                "            display: inline-block;" +
                                "            padding: 12px 22px;" +
                                "            background: #667eea;" +
                                "            color: white;" +
                                "            border-radius: 8px;" +
                                "            text-decoration: none;" +
                                "            font-weight: bold;" +
                                "        }" +
                                "        .voltar:hover {" +
                                "            background: #5568d8;" +
                                "        }" +
                                "    </style>" +
                                "</head>" +
                                "<body>" +
                                "    <div class='card'>" +
                                "        <div class='icone'>⚠️</div>" +
                                "        <h1>URL inválida</h1>" +
                                "        <p>A URL informada está em branco ou não é válida.</p>" +
                                "        <a class='voltar' href='/'>← Tentar novamente</a>" +
                                "    </div>" +
                                "</body>" +
                                "</html>");
                    }
                } else {
                    ctx.html("<html>" +
                            "<head>" +
                            "    <meta charset='UTF-8'>" +
                            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                            "    <title>Erro</title>" +
                            "    <style>" +
                            "        * { box-sizing: border-box; }" +
                            "        body {" +
                            "            margin: 0;" +
                            "            font-family: Arial, sans-serif;" +
                            "            background: linear-gradient(135deg, #667eea, #764ba2);" +
                            "            min-height: 100vh;" +
                            "            display: flex;" +
                            "            justify-content: center;" +
                            "            align-items: center;" +
                            "        }" +
                            "        .card {" +
                            "            background: white;" +
                            "            width: 90%;" +
                            "            max-width: 500px;" +
                            "            padding: 40px;" +
                            "            border-radius: 16px;" +
                            "            box-shadow: 0 15px 40px rgba(0,0,0,0.25);" +
                            "            text-align: center;" +
                            "        }" +
                            "        .icone {" +
                            "            font-size: 50px;" +
                            "            margin-bottom: 15px;" +
                            "        }" +
                            "        h1 {" +
                            "            color: #d9534f;" +
                            "            margin: 0 0 15px 0;" +
                            "        }" +
                            "        p {" +
                            "            color: #666;" +
                            "            font-size: 17px;" +
                            "            margin-bottom: 25px;" +
                            "        }" +
                            "        .voltar {" +
                            "            display: inline-block;" +
                            "            padding: 12px 22px;" +
                            "            background: #667eea;" +
                            "            color: white;" +
                            "            border-radius: 8px;" +
                            "            text-decoration: none;" +
                            "            font-weight: bold;" +
                            "        }" +
                            "        .voltar:hover {" +
                            "            background: #5568d8;" +
                            "        }" +
                            "    </style>" +
                            "</head>" +
                            "<body>" +
                            "    <div class='card'>" +
                            "        <div class='icone'>⚠️</div>" +
                            "        <h1>URL inválida</h1>" +
                            "        <p>A URL informada está em branco ou não é válida.</p>" +
                            "        <a class='voltar' href='/'>← Tentar novamente</a>" +
                            "    </div>" +
                            "</body>" +
                            "</html>");
                }
            });

        }).start(7070);
    }
}