package controler;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import model.Anime;
import model.Episode;

public class Controler {
	
	private static String localPath;
	private static String siteURL;
	private static final String defaultlocalPath = System.getenv("UserProfile") + "\\Documents\\AnimeTracker";
	private static final String defaultSiteURL = "https://gogoanime.gr/";
	
	public static String getLocalPath() {
		return localPath;
	}

	public static void setLocalPath(String localPath) {
		Controler.localPath = localPath;
		writeSettings();
	}
	
	public static String getLocalPathDefault() {
		return defaultlocalPath;
	}

	public static String getSiteURL() {
		return siteURL;
	}

	public static void setSiteURL(String siteURL) {
		Controler.siteURL = siteURL;
		writeSettings();
	}
	
	public static String getSiteURLDefault() {
		return defaultSiteURL;
	}
	
	public static void readSettings() {
		File settingsFile;
		try {
			settingsFile = createSettingsFile();
			if(!settingsFile.exists()) {
				settingsFile.getParentFile().mkdirs();
				settingsFile.createNewFile();
				BufferedWriter animeInfoFileWriter = new BufferedWriter(new FileWriter(settingsFile));
			    animeInfoFileWriter.write(defaultlocalPath);
			    animeInfoFileWriter.write("\n");
			    animeInfoFileWriter.write(defaultSiteURL);
			    animeInfoFileWriter.close();
			}
			BufferedReader animeInfoFileReader = new BufferedReader(new FileReader(settingsFile));
			localPath = animeInfoFileReader.readLine();
			siteURL = animeInfoFileReader.readLine();
			animeInfoFileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeSettings() {
		File settingsFile;
		try {
			settingsFile = createSettingsFile();
			if(!settingsFile.exists()) {
				settingsFile.getParentFile().mkdirs();
				settingsFile.createNewFile();
			}
			BufferedWriter animeInfoFileWriter = new BufferedWriter(new FileWriter(settingsFile));
		    animeInfoFileWriter.write(localPath);
		    animeInfoFileWriter.write("\n");
		    animeInfoFileWriter.write(siteURL);
		    animeInfoFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void checkEpisodeStatus(Episode episode) {
		System.out.println("Checking episode status " + episode);
		URL url;
		HttpsURLConnection con;
		int responseStatus;
		String response;
		URL downloadSiteLink;
		try {
			//check episode watched
			File episodeWatchedFile = createEpisodeWatchedFile(episode);
			System.out.println(episodeWatchedFile);
			episode.setWatched(episodeWatchedFile.exists());
			if(episode.isWatched())
				System.out.println("episode watched");
			else
				System.out.println("episode not watched");
			//check episode in files
			File episodeFile = createEpisodeFile(episode);
			if (episodeFile.exists()) {
				//check episode downloading
				File episodeDownloadingFile = createEpisodeDownloadingFile(episode);
				if (episodeDownloadingFile.exists()) {
					episode.setFileLocation(episodeFile);
					episode.setStatus(Episode.ABORT);
					System.out.println("episode abort");
					return;
				}
				episode.setFileLocation(episodeFile);
				episode.setStatus(Episode.IN_FILES);
				System.out.println("episode in files");
				return;
			}
			//create connection with episode site
			url = createEpisodeURL(episode);
			System.out.println(url);
			con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			responseStatus = con.getResponseCode();
			//check if episode site not exist
			if (responseStatus < 200 || responseStatus > 299) {
				episode.setStatus(Episode.NOT_EXIST);
				System.out.println("episode not exist");
				return;
			}
			//get response site
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer bufferedResponse = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				bufferedResponse.append(inputLine);
			}
			in.close();
			response = bufferedResponse.toString();
			//find download site link in response
			int dowloads = response.indexOf("class=\"dowloads\"");
			int ahref = response.indexOf("href=\"", dowloads) + 6;
			int ahrefEnd = response.indexOf("\"", ahref);
			downloadSiteLink = new URL(response.substring(ahref, ahrefEnd));
			episode.setDownloadSiteLink(downloadSiteLink);
			episode.setStatus(Episode.AVAIBLE);
			System.out.println("episode avaiblet");
		} catch (Exception e) {
			episode.setStatus(Episode.ERROR);
			System.out.println("episode error");
			e.printStackTrace();
		}
	}
	
	public static void passFileLink(String fileLinkString, Episode episode) {
		URL fileLink;
		try {
			if (episode.getStatus() != Episode.AVAIBLE)
				throw new Exception("Episode not avaible");
			fileLink = new URL(fileLinkString);
			episode.setFileLink(fileLink);
			episode.setStatus(Episode.FILE_LINK_PASSED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void downloadEpisode(Episode episode, ProgressListener progressListener) {
		episodeDownloading(episode, true);
		FileOutputStream fileOutputStream;
		ReadableByteChannel readableByteChannel;
		URL url;
		File file;
		try {
			if (episode.getStatus() != Episode.FILE_LINK_PASSED)
				throw new Exception("Episode file's link not passed");
			url = episode.getFileLink();
			int episodeSize = getEpisodeSize(url);
			readableByteChannel = new CallbackByteChannel(Channels.newChannel(url.openStream()), episodeSize, episode, progressListener);
			file = createEpisodeFile(episode);
			file.getParentFile().mkdirs();
			file.createNewFile();
			episode.setFileLocation(file);
			episode.setStatus(Episode.DOWNLOADING);
			fileOutputStream = new FileOutputStream(file);
			long transfered = fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			readableByteChannel.close();
			fileOutputStream.close();
			System.out.println(episodeSize + " " + transfered);
			if(transfered < episodeSize) {
				episode.setStatus(Episode.ABORT);
			} else {
				episode.setStatus(Episode.IN_FILES);
				episodeDownloading(episode, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static File createSettingsFile() {
		String fileLocation = "";
		fileLocation += System.getenv("APPDATA");
		fileLocation += "\\animeTracker\\settings.txt";
		File file = new File(fileLocation);
		return file;
	}
	
	private static URL createEpisodeURL(Episode episode) {
		String episodeUrl;
		URL url = null;
		try {
			episodeUrl = "";
			episodeUrl += siteURL;
			episodeUrl += "/";
			episodeUrl += (episode.getType() == Episode.DUB ? episode.getAnime().getDubName() : episode.getAnime().getName());
			episodeUrl += "-episode-";
			episodeUrl += episode.getNumber();
			url = new URL(episodeUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	private static File createEpisodeFile(Episode episode) {
		String fileLocation = "";
		fileLocation += localPath;
		fileLocation += "\\";
		fileLocation += episode.getAnime().getName();
		fileLocation += "\\";
		fileLocation += episode.getAnime().getName();
		fileLocation += (episode.getType() == Episode.DUB ? "-dub" : "");
		fileLocation += "-episode-";
		fileLocation += episode.getNumber();
		fileLocation += ".mp4";
		File file = new File(fileLocation);
		return file;
	}
	
	private static File createEpisodeWatchedFile(Episode episode) {
		String fileLocation = "";
		fileLocation += localPath;
		fileLocation += "\\";
		fileLocation += episode.getAnime().getName();
		fileLocation += "\\";
		fileLocation += episode.getAnime().getName();
		fileLocation += "-episode-";
		fileLocation += episode.getNumber();
		fileLocation += ".info";
		File file = new File(fileLocation);
		return file;
	}
	
	private static File createEpisodeDownloadingFile(Episode episode) {
		String fileLocation = "";
		fileLocation += localPath;
		fileLocation += "\\";
		fileLocation += episode.getAnime().getName();
		fileLocation += "\\";
		fileLocation += episode.getAnime().getName();
		fileLocation += "-episode-";
		fileLocation += episode.getNumber();
		fileLocation += "-downloading.info";
		File file = new File(fileLocation);
		return file;
	}
	
	private static File createAnimeDirectory(Anime anime) {
		String fileLocation = "";
		fileLocation += localPath;
		fileLocation += "\\";
		fileLocation += anime.getName();
		File file = new File(fileLocation);
		return file;
	}
	
	private static File createAnimeInfoFile(Anime anime) {
		String fileLocation = "";
		fileLocation += localPath;
		fileLocation += "\\";
		fileLocation += anime.getName();
		fileLocation += "\\";
		fileLocation += anime.getName();
		fileLocation += ".info";
		File file = new File(fileLocation);
		return file;
	}
	
	private static int getEpisodeSize(URL url) {
		HttpURLConnection connection;
		int contentLength = -1;
		try {
			connection = (HttpURLConnection) url.openConnection();
			contentLength = connection.getContentLength();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contentLength;
	}

	public static void openDownloadSite(Episode episode) {
		try {
			if (episode.getStatus() != Episode.AVAIBLE)
				throw new Exception("Episode not avaible");
			Desktop.getDesktop().browse(episode.getDownloadSiteLink().toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void playEpisode(Episode episode) {
		try {
			if (episode.getStatus() < Episode.DOWNLOADING)
				throw new Exception("Episode not in files");
			Desktop.getDesktop().open(episode.getFileLocation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteEpisode(Episode episode) {
		try {
			if (episode.getStatus() < Episode.DOWNLOADING)
				throw new Exception("Episode not in files");
			episode.getFileLocation().delete();
			episode.setStatus(Episode.DELETED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void episodeWatched(Episode episode, boolean watched) {
		File watchedFile;
		try {
			watchedFile = createEpisodeWatchedFile(episode);
			if(watched) {
				watchedFile.getParentFile().mkdirs();
				watchedFile.createNewFile();
			}
			else
				watchedFile.delete();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void episodeDownloading(Episode episode, boolean downloading) {
		File downloadingFile;
		try {
			downloadingFile = createEpisodeDownloadingFile(episode);
			if(downloading) {
				downloadingFile.getParentFile().mkdirs();
				downloadingFile.createNewFile();
			}
			else
				downloadingFile.delete();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Anime> getAnimeList() {
		File localPathDir = new File(localPath);
		if(!localPathDir.exists())
			localPathDir.mkdir();
		ArrayList<Anime> animes = null;
		File[] animeDirs;
		try {
			animes = new ArrayList<Anime>();
			animeDirs = localPathDir.listFiles();
			for(File animeDir : animeDirs) {
				Anime anime = new Anime(animeDir.getName());
				File animeInfoFile = createAnimeInfoFile(anime);
				if(animeInfoFile.exists()) {
					BufferedReader animeInfoFileReader = new BufferedReader(new FileReader(animeInfoFile));
					anime.setDubName(animeInfoFileReader.readLine());
					animeInfoFileReader.close();
				}
				animes.add(anime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return animes;
	}
	
	public static void addAnime(Anime anime) {
		File animeDir, animeInfoFile;
		try {
			animeDir = createAnimeDirectory(anime);
			animeDir.mkdir();
			animeInfoFile = createAnimeInfoFile(anime);
			animeInfoFile.createNewFile();
		    BufferedWriter animeInfoFileWriter = new BufferedWriter(new FileWriter(animeInfoFile));
		    animeInfoFileWriter.write(anime.getDubName());
		    animeInfoFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void editAnime(Anime anime) {
		File animeDir, animeInfoFile;
		try {
			animeDir = createAnimeDirectory(anime);
			if(!animeDir.exists())
				animeDir.mkdir();
			animeInfoFile = createAnimeInfoFile(anime);
			if(!animeDir.exists())
				animeInfoFile.createNewFile();
		    BufferedWriter animeInfoFileWriter = new BufferedWriter(new FileWriter(animeInfoFile));
		    animeInfoFileWriter.write(anime.getDubName());
		    animeInfoFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAnime(Anime anime) {
		File animeDir = createAnimeDirectory(anime);
		File[] files = animeDir.listFiles();
		for(File file : files) {
			file.delete();
		}
		animeDir.delete();
	}
	
	public interface ProgressListener {
		public void progressChanged(CallbackByteChannel rbc, Episode episode, int progress, int eta);
	}
}

