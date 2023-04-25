package model;

import java.io.File;
import java.net.URL;

public class Episode {

	public static final int SUB = 0;
	public static final int DUB = 1;
	
	public static final int ERROR = -3;
	public static final int UNKNOWN = -2;
	public static final int DELETED = -1;
	public static final int NOT_EXIST = 0;
	public static final int AVAIBLE = 1;
	public static final int FILE_LINK_PASSED = 2;
	public static final int DOWNLOADING = 3;
	public static final int ABORT = 4;
	public static final int IN_FILES = 5;
	
	private Anime anime;
	private int type;
	private int number;
	private int status;
	private boolean watched;
	private URL downloadSiteLink;
	private URL fileLink;
	private File fileLocation;
	
	public Episode(Anime anime, int type, int number) {
		this.anime = anime;
		this.type = type;
		this.number = number;
		status = UNKNOWN;
		watched = false;
		downloadSiteLink = null;
		fileLink = null;
		fileLocation = null;
		if(type == SUB)
			anime.addSubEpisode(this);
		else if(type == DUB)
			anime.addDubEpisode(this);
	}
	
	public Episode(Anime anime, int type, int number, int status, boolean watched) {
		this.anime = anime;
		this.type = type;
		this.number = number;
		this.status = status;
		this.watched = watched;
		downloadSiteLink = null;
		fileLink = null;
		fileLocation = null;
		if(type == SUB)
			anime.addSubEpisode(this);
		else if(type == DUB)
			anime.addDubEpisode(this);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isWatched() {
		return watched;
	}

	public void setWatched(boolean watched) {
		this.watched = watched;
	}

	public URL getDownloadSiteLink() {
		return downloadSiteLink;
	}

	public void setDownloadSiteLink(URL downloadSiteLink) {
		this.downloadSiteLink = downloadSiteLink;
	}

	public URL getFileLink() {
		return fileLink;
	}

	public void setFileLink(URL fileLink) {
		this.fileLink = fileLink;
	}

	public File getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(File fileLocation) {
		this.fileLocation = fileLocation;
	}

	public Anime getAnime() {
		return anime;
	}

	public int getType() {
		return type;
	}

	public int getNumber() {
		return number;
	}
	
	public String toString() {
		return anime.getName() + " " + type + " " + number + " " + status;
	}
}
