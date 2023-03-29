package com.functions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class HelloWorld implements HttpFunction {
  private static final Logger logger = Logger.getLogger(HelloWorld.class.getName());

  private static final Gson gson = new Gson();

  //  Simple function to return "Hello World"
  @Override
  public void service(HttpRequest request, HttpResponse response) throws IOException {
    String name = request.getFirstQueryParameter("name").orElse("world");

    try {
      JsonElement requestParsed = gson.fromJson(request.getReader(), JsonElement.class);
      JsonObject requestJson = null;

      if (requestParsed != null && requestParsed.isJsonObject()) {
        requestJson = requestParsed.getAsJsonObject();
      }

      if (requestJson != null && requestJson.has("name")) {
        name = requestJson.get("name").getAsString();
      }
    } catch (JsonParseException e) {
      logger.severe("Error parsing JSON: " + e.getMessage());
    }

    var writer = new PrintWriter(response.getWriter());
    writer.printf("Hello %s!", name);
  }
}