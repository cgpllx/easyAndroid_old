package com.kubeiwu.httphelper.cache;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.kubeiwu.httphelper.cache.dislrucache.DiskLruCache;

import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import android.content.Context;
import android.util.Log;

/**
 * retrofit 缓存辅助类
 * 
 * @author Administrator
 *
 */
public class KOkhttpCache extends DiskLruCacheHelper {

	public KOkhttpCache(Context context) throws IOException {
		super(context);
	}

	public KOkhttpCache(Context context, int cacheVersion) throws IOException {
		super(context, cacheVersion);
	}

	public KOkhttpCache(Context context, String dirName, int cacheVersion) throws IOException {
		super(context, dirName, cacheVersion);
	}

	public void put(String url, TypedInput typedInput) {
		OutputStream out = null;
		DiskLruCache.Editor editor = null;
		try {
			editor = editor(url);
			if (editor == null) {
				return;
			}

			out = editor.newOutputStream(0);
			String mimetype = typedInput.mimeType();
			long length = typedInput.length();
			InputStream in = typedInput.in();

			IOUtils.writeString(out, mimetype);
			IOUtils.writeLong(out, length);
			copy(in, out);

			out.flush();
			editor.commit();// write CLEAN
		} catch (Exception e) {
			e.printStackTrace();
			try {
				editor.abort();// write REMOVE
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public TypedInput getAsTypedInput(String url) {
		CountingInputStream cis = null;
		try {
			DiskLruCache.Snapshot snapshot = mDiskLruCache.get(Utils.hashKeyForDisk(url));
			if (snapshot == null) // not find entry , or entry.readable = false
			{
				Log.e(TAG, "not find cache file , or file.readable = false");
				return null;
			}
			cis = new CountingInputStream(snapshot.getInputStream(0));
			String mimeType = IOUtils.readString(cis);
			long length = IOUtils.readLong(cis);// 暂时没有用到，先放在这里
			byte[] data = streamToBytes(cis, (int) (snapshot.getLength(0) - cis.bytesRead));
			System.out.println("data===" + data.length);
			System.out.println("snapshot===" + snapshot.getLength(0));
			System.out.println("length===" + length);
			TypedByteArray typedByteArray = new TypedByteArray(mimeType, data);
			return typedByteArray;

		} catch (IOException e) {
			remove(url);
			return null;
		} finally {
			if (cis != null) {
				try {
					cis.close();
				} catch (IOException ioe) {
					return null;
				}
			}
		}
	}

	/**
	 * Reads the contents of an InputStream into a byte[].
	 * */
	private byte[] streamToBytes(InputStream in, int length) throws IOException {
		byte[] bytes = new byte[length];
		int count;
		int pos = 0;
		while (pos < length && ((count = in.read(bytes, pos, length - pos)) != -1)) {
			pos += count;
		}
		if (pos != length) {
			throw new IOException("Expected " + length + " bytes, read " + pos + " bytes");
		}
		return bytes;
	}

	private static class CountingInputStream extends FilterInputStream {
		private int bytesRead = 0;

		private CountingInputStream(InputStream in) {
			super(in);
		}

		@Override
		public int read() throws IOException {
			int result = super.read();
			if (result != -1) {
				bytesRead++;
			}
			return result;
		}

		@Override
		public int read(byte[] buffer, int offset, int count) throws IOException {
			int result = super.read(buffer, offset, count);
			if (result != -1) {
				bytesRead += result;
			}
			return result;
		}
	}
}
