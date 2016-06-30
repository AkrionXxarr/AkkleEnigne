package core.resource.audio;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import core.resource.ResourceManager;

// Audio that's to be fully loaded into memory.
//
public class AudioClip
{
	// Audio clip information
	private static class Data
	{
		public byte[] buffer;
		public AudioFormat format;
	}
	
	private final int id;
	private Data data;
	
	public AudioClip(String filePath, String fileName)
	{
		// Form an ID for the resource manager using a hash of the full file path
		id = (filePath + fileName).hashCode();
		
		ResourceManager.ResourceData resData = ResourceManager.getResource(AudioClip.class, id);
		
		// If the resource doesn't exist, create a new one, 
		// otherwise get a reference to the existing resource.
		if (resData.resource == null)
		{
			data = new Data();
			
			try
			{
				AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filePath + fileName));
				data.format = ais.getFormat();
				DataInputStream dis = new DataInputStream(ais);
				
				data.buffer = new byte[(int)(ais.getFrameLength() * data.format.getFrameSize())];
				
				dis.readFully(data.buffer);
				dis.close();
				ais.close();
			} catch (UnsupportedAudioFileException | IOException e)
			{
				e.printStackTrace();
			}
			
			
			resData.resource = data;
		}
		else
		{
			data = (AudioClip.Data)resData.resource;
		}
		
		
	}
	
	@Override
	public void finalize()
	{
		ResourceManager.releaseResource(AudioClip.class, id);
	}
	
	public byte[] getBuffer()
	{
		return data.buffer;
	}
	
	public AudioFormat getAudioFormat()
	{
		return data.format;
	}
}
