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
		// Verificar si el HTML es nulo o vacío
		if (html == null || html.isEmpty()) {
			return "";
		}

		// Utilizar JSoup para parsear el HTML y extraer el texto sin etiquetas HTML
		Document doc = Jsoup.parse(html);
		String text = doc.text();

		// Reemplazar <br> con saltos de línea reales
		text = text.replaceAll("\\\\n", "\n");
		text = text.replaceAll("(?i)<br[^>]*>", "\n"); // Expresión regular para reemplazar <br> con \n

		return text;
	}

}