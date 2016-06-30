package core.resource.audio;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

// Deals with setting up the data for javax to play the audio
// which runs in a self-terminating thread.
//
public class AudioManager
{	
	public static float clipVolume = 0;
	public static float streamVolume = 0;

	public static void play(AudioClip audioClip)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{	
					SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class,  audioClip.getAudioFormat(), audioClip.getBuffer().length / audioClip.getAudioFormat().getFrameSize());
					SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
					
					line.open(audioClip.getAudioFormat());					
					line.start();
					
					FloatControl control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
					
					control.setValue(clipVolume);
					
					byte[] buffer = audioClip.getBuffer();
					
					int offset = 0;
					while (offset < audioClip.getBuffer().length)
					{
						offset += line.write(buffer, offset, buffer.length);
					}
					
					line.drain();
					line.stop();
					line.close();
				} catch (LineUnavailableException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void play(AudioStream audioStream)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{	
					AudioInputStream ais = AudioSystem.getAudioInputStream(audioStream.getFile());
					AudioFormat af = ais.getFormat();
					
					SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class,  af, (int)(ais.getFrameLength() * af.getFrameSize()));
					SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
					
					line.open(af);
					line.start();
					
					FloatControl control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
					control.setValue(streamVolume);

					int numRead = 0;
					byte[] buf = new byte[line.getBufferSize()];
					while ((numRead = ais.read(buf, 0, buf.length)) >= 0)
					{
						int offset = 0;
						while (offset < numRead)
						{
							control.setValue(streamVolume);
							offset += line.write(buf,  offset,  numRead - offset);
						}
					}
					
					line.drain();
					line.stop();
					line.close();
				} catch (LineUnavailableException e)
				{
					e.printStackTrace();
				} catch (UnsupportedAudioFileException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
