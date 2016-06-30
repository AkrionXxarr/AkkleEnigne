package core.resource.audio;

import java.io.File;

// Audio that's streamed from a source.
//
public class AudioStream
{
	File file;
	
	public AudioStream(String filePath, String fileName)
	{
		file = new File(filePath + fileName);
	}
	
	public File getFile()
	{
		return file;
	}
}
