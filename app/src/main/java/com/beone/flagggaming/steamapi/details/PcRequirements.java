package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PcRequirements{

	@SerializedName("minimum")
	private String minimum;

	@SerializedName("recommended")
	private String recommended;

	public String getMinimum(){
		return stripHtml(minimum);
	}

	public String getRecommended(){
		return stripHtml(recommended);
	}

	// Método para eliminar etiquetas HTML de un texto y reemplazar <br> con saltos de línea
	private String stripHtml(String html) {
		// Utilizar JSoup para parsear el HTML y extraer el texto sin etiquetas HTML
		Document doc = Jsoup.parse(html);
		String text = doc.text();

		// Reemplazar <br> con saltos de línea reales
		text = text.replaceAll("\\\\n", "\n");
		text = text.replaceAll("(?i)<br[^>]*>", "\n"); // Expresión regular para reemplazar <br> con \n

		return text;
		/*
		//QUITAR TODO HTML
		// Expresión regular para encontrar etiquetas HTML
		String regex = "<[^>]*>";
		// Compilar la expresión regular en un patrón
		Pattern pattern = Pattern.compile(regex);
		// Crear un objeto Matcher para el texto dado y el patrón
		Matcher matcher = pattern.matcher(html);
		// Reemplazar todas las etiquetas HTML con una cadena vacía
		String textoSinHtml = matcher.replaceAll("");
		// Reemplazar <br> con saltos de línea
		textoSinHtml = textoSinHtml.replaceAll("<br>", "\n");
		// Retornar el texto sin etiquetas HTML
		return textoSinHtml;

		 */
	}
}