package controler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import model.Episode;

public class CallbackByteChannel implements ReadableByteChannel {
	Controler.ProgressListener progressListener;
	long size;
	ReadableByteChannel rbc;
	long sizeRead;
	String link;
	Episode episode;
	int lastProgress;
	long lastProgressTime;
	long meanProgressTime;

	CallbackByteChannel(ReadableByteChannel rbc, long expectedSize, Episode episode, Controler.ProgressListener progressListener) {
		this.progressListener = progressListener;
		this.size = expectedSize;
		this.rbc = rbc;
		this.episode = episode;
		lastProgress = 0;
		lastProgressTime = System.currentTimeMillis();
		meanProgressTime = 0;
	}

	public void close() throws IOException {
		rbc.close();
	}

	public long getReadSoFar() {
		return sizeRead;
	}

	public boolean isOpen() {
		return rbc.isOpen();
	}

	public int read(ByteBuffer bb) throws IOException {
		int n;
		int progress; // [promils]
		long now; // [ms]
		long time; //[ms]
		int eta; // [s]
		if ((n = rbc.read(bb)) > 0) {
			sizeRead += n;
			progress = size > 0 ? (int) (1000 * sizeRead / size) : -1;
			if(progress > lastProgress) {
				now = System.currentTimeMillis();
				time = now - lastProgressTime;
				lastProgress = progress;
				lastProgressTime = now;
				meanProgressTime = (meanProgressTime * (progress-1) + time) / progress;
				eta = (int) ((1000-progress)*meanProgressTime/1000);
				progressListener.progressChanged(this, episode, progress, eta);
			}
		}
		return n;
	}
}
