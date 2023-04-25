package model;

import java.util.ArrayList;

public class Anime {
	private String name;
	private String dubName;
	private ArrayList<Episode> subEpisodes;
	private ArrayList<Episode> dubEpisodes;
	
	public Anime(String name) {
		super();
		this.name = name;
		this.dubName = name + "-dub";
		this.subEpisodes = new ArrayList<Episode>();
		this.dubEpisodes = new ArrayList<Episode>();
	}
	
	public Anime(String name, String dubName) {
		super();
		this.name = name;
		this.dubName = dubName;
		this.subEpisodes = new ArrayList<Episode>();
		this.dubEpisodes = new ArrayList<Episode>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDubName() {
		return dubName;
	}

	public void setDubName(String dubName) {
		this.dubName = dubName;
	}

	public ArrayList<Episode> getSubEpisodes() {
		return subEpisodes;
	}

	public ArrayList<Episode> getDubEpisodes() {
		return dubEpisodes;
	}

	public int getNumberOfWatchedSubEpisodes() {
		int count = 0;
		for(Episode episode : subEpisodes)
			if(episode.isWatched())
				count++;
		return count;
	}

	public int getNumberOfWatchedDubEpisodes() {
		int count = 0;
		for(Episode episode : dubEpisodes)
			if(episode.isWatched())
				count++;
		return count;
	}

	public int getNumberOfAvaibleSubEpisodes() {
		int count = 0;
		for(Episode episode : subEpisodes)
			if(episode.getStatus() <= Episode.AVAIBLE)
				count++;
		return count;
	}

	public int getNumberOfAvaibleDubEpisodes() {
		int count = 0;
		for(Episode episode : dubEpisodes)
			if(episode.getStatus() <= Episode.AVAIBLE)
				count++;
		return count;
	}
	
	protected void addSubEpisode(Episode episode) {
		subEpisodes.add(episode);
	}
	
	protected void addDubEpisode(Episode episode) {
		subEpisodes.add(episode);
	}
}
