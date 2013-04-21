package se.s8n.utils.movehier;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	if (args.length != 2) {
    		System.err.println("Usage: movehier <pattern> <source> <dest>");
    		System.exit(1);
    	}
    	final Path source = FileSystems.getDefault().getPath(args[1]);
    	final Path dest = FileSystems.getDefault().getPath(args[1]);
    	final PathMatcher matcher = FileSystems.getDefault().getPathMatcher(args[2]);
    	if (!Files.exists(source)) {
    		System.err.println("Source not exist");
    		System.exit(1);
    	}
    	if (!Files.exists(dest)) {
    		System.err.println("Destination not exist");
    		System.exit(1);
    	}
        Files.walkFileTree(source,
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                        throws IOException
                    {
                        Path targetdir = dest.resolve(source.relativize(dir));
                        try {
                            Files.copy(dir, targetdir);
                        } catch (FileAlreadyExistsException e) {
                             if (!Files.isDirectory(targetdir))
                                 throw e;
                        }
                        return FileVisitResult.CONTINUE;
                    }
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException
                    {
                    	final Path baseName = file.getFileName();
                    	if (matcher.matches(baseName)) {
                    		Path destPath = dest.resolve(source.relativize(file));
                    		System.out.println("Moving " + file + " to " + destPath);
//                    		Files.move(file, destPath);
                    	}
                        return FileVisitResult.CONTINUE;
                    }
                });
    }
}
