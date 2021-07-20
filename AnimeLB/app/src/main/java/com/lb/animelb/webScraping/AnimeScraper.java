package com.lb.animelb.webScraping;

import com.lb.animelb.clases.AnimeInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimeScraper {
    final static String RUTA_INICIO = "https://tioanime.com";
    final static String RUTA_ANIMES = "https://tioanime.com/directorio?";
    final static String GENRE = "genero=";
    final static String PAGE = "&p=";
    final static String QUERY = "q=";


    public static ArrayList<AnimeInfo> getAllAnimes(int pageNumber) {
        ArrayList<AnimeInfo> animes = new ArrayList<>();

        Document doc = getHtmlDocument(RUTA_ANIMES + PAGE + pageNumber);
        Elements elements = doc.select("article");

        String title, imageUrl, animeUrl;
        for (Element element : elements) {
            title = element.getElementsByClass("anime").text();
            imageUrl = RUTA_INICIO + element.getElementsByTag("img").attr("src");
            animeUrl = RUTA_INICIO + element.getElementsByTag("a").attr("href");

            animes.add(new AnimeInfo(title, imageUrl, animeUrl));
        }

        return animes;
    }

    public static ArrayList<AnimeInfo> getAnimesSearch(int pageNumber, String animeName) {
        ArrayList<AnimeInfo> animes = new ArrayList<>();

        Document doc = getHtmlDocument(RUTA_ANIMES + QUERY + animeName + PAGE + pageNumber);
        Elements elements = doc.select("article");

        String title, imageUrl, animeUrl;
        for (Element element : elements) {
            title = element.getElementsByClass("anime").text();
            imageUrl = RUTA_INICIO + element.getElementsByTag("img").attr("src");
            animeUrl = RUTA_INICIO + element.getElementsByTag("a").attr("href");

            animes.add(new AnimeInfo(title, imageUrl, animeUrl));
        }

        return animes;
    }

    public static ArrayList<AnimeInfo> getAnimesByGenre(String genre, int pageNumber) {
        ArrayList<AnimeInfo> animes = new ArrayList<>();

        Document doc = getHtmlDocument(RUTA_ANIMES + GENRE + genre + PAGE + pageNumber);
        Elements elements = doc.select("article");

        String title, imageUrl, animeUrl;
        for (Element element : elements) {
            title = element.getElementsByClass("anime").text();
            imageUrl = RUTA_INICIO + element.getElementsByTag("img").attr("src");
            animeUrl = RUTA_INICIO + element.getElementsByTag("a").attr("href");

            animes.add(new AnimeInfo(title, imageUrl, animeUrl));
        }

        return animes;
    }

    public static ArrayList<String> getEpisodesUrl(String ruta) {
        ArrayList<String> episodesUrl = new ArrayList<>();
        String ruta_ep = ruta.replace("/anime", "/ver");

        Document doc = getHtmlDocument(ruta);
        Elements linksOnPage = doc.select("script");

        Matcher matcher;
        Pattern pattern = Pattern.compile("episodes = (.*);");

        for (Element element : linksOnPage) {
            for (DataNode node : element.dataNodes()) {
                matcher = pattern.matcher(node.getWholeData());
                while (matcher.find()) {
                    String[] episodios = Objects.requireNonNull(matcher.group(1)).split(",");
                    for (int i = 1; i <= episodios.length; i++) {
                        episodesUrl.add(ruta_ep + "-" + i);
                    }
                }
            }
        }

        return episodesUrl;
    }

    public static String getEpisodeDescription(String ruta) {
        String description = "";
        Document doc = getHtmlDocument(ruta);
        Elements elements = doc.select("meta");
        for (Element element : elements) {
            if (element.attr("property").equals("og:description")) {
                description = element.attr("content").replace("- TioAnime animes de Calidad ", "").trim();
                break;
            }
        }

        if (description.isEmpty())
            return "Descripción no disponible";
        else
            return description;
    }

    public static String getVideoPlayerUrl(String episodeUrl) {
        Document doc = getHtmlDocument(episodeUrl);
        Elements linksOnPage = doc.select("script");

        Matcher matcher;
        Pattern pattern = Pattern.compile("videos = (.*);");

        for (Element element : linksOnPage){
            for (DataNode node : element.dataNodes()){
                matcher = pattern.matcher(node.getWholeData());
                while (matcher.find()){
                    String[] playersUrl = matcher.group(1).split(",");
                    for (int i = 1; i < playersUrl.length; i++) {
                        String playerName = playersUrl[i-1];
                        if (playerName.contains("Umi") || playerName.contains("Maru") || playerName.contains("Streamium") ) {
                            String playerUrl = playersUrl[i].replace("\"", "");
                            return playerUrl;
                        }
                    }
                }
            }
        }

        return "";
    }

    private static Document getHtmlDocument(String url) {
        Document[] doc = {null};

        Thread htmlGetterThread = new Thread(() -> {
            try {
                doc[0] = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
            } catch (IOException ex) {
                System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
            }
        });
        htmlGetterThread.start();

        try {
            htmlGetterThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return doc[0];
    }
}
