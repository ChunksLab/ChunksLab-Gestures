package com.chunkslab.gestures.paper;

import com.chunkslab.gestures.api.config.ConfigFile;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class PaperGesturesPluginLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        for (String library : resolveLibraries(classpathBuilder)) {
            resolver.addDependency(new Dependency(new DefaultArtifact(library), null));
        }

        resolver.addRepository(new RemoteRepository.Builder(
                "central", "default", "https://repo.maven.apache.org/maven2/"
        ).build());

        classpathBuilder.addLibrary(resolver);
    }

    @NotNull
    private static List<String> resolveLibraries(@NotNull PluginClasspathBuilder classpathBuilder) {
        try {
            ConfigFile config = new ConfigFile(PaperGesturesPlugin.getInstance(), "paper-libraries.yml", true);

            List<String> libraries = config.getStringList("libraries");
            if (libraries.isEmpty()) {
                classpathBuilder.getContext().getLogger().warn("No libraries defined in paper-libraries.yml.");
                return Collections.emptyList();
            }

            return libraries;
        } catch (Exception e) {
            classpathBuilder.getContext().getLogger().error("Failed to load libraries from paper-libraries.yml", e);
            return Collections.emptyList();
        }
    }
}
