package org.jurr.liquibase.releaseplugin;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public final class Utils
{
	private Utils()
	{
	}

	@Nonnull
	public static Path resolveIncludeFile(@Nonnull final Path masterFile, @Nonnull final Path includeFile, final boolean relativeToChangelogFile)
	{
		if (!relativeToChangelogFile)
		{
			return includeFile;
		}

		return masterFile.resolveSibling(includeFile);
	}

	@Nonnull
	public static Path replaceStringInPath(@Nonnull final Path input, @Nonnull final String token, @Nonnull final String replacement)
	{
		return Paths.get(input.toString().replace(token, replacement));
	}

	public static void skipUntillEndElement(@Nonnull final XMLEventReader xmlEventReader, @Nonnull final StartElement startElement) throws XMLStreamException
	{
		while (xmlEventReader.hasNext())
		{
			final XMLEvent xmlEvent = xmlEventReader.nextEvent();
			if (xmlEvent.isEndElement())
			{
				final EndElement endElement = (EndElement) xmlEvent;
				if (endElement.getName().equals(startElement.getName()))
				{
					return;
				}
			}
		}
		throw new IllegalStateException("End of document reached while searching for end tag for " + startElement.getName());
	}
}