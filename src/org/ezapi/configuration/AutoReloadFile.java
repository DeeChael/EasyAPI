package org.ezapi.configuration;

import com.sun.nio.file.SensitivityWatchEventModifier;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.ezapi.function.NonReturn;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public final class AutoReloadFile {

    private final File file;

    private BukkitTask task;

    private NonReturn onModified = this::emptyMethod;

    private NonReturn onDeleted = this::emptyMethod;

    private NonReturn onCreated = this::emptyMethod;

    private WatchService watchService;

    private void emptyMethod() {}

    /**
     * Listening to a file and call event when the file was modified or deleted
     *
     * @param plugin your plugin instance
     * @param file the file to listen
     */
    public AutoReloadFile(Plugin plugin, File file) {
        if (file.isDirectory()) throw new IllegalArgumentException("The file must be a file not a directory");
        if (file.getParentFile() == null) throw new IllegalArgumentException("The file's parent cannot be null");
        this.file = file;
        init(plugin);
    }

    /**
     * Called when file has been modified
     * @param method on modified
     */
    public void onModified(NonReturn method) {
        this.onModified = method;
    }

    /**
     * Called when file has been deleted
     * @param method on deleted
     */
    public void onDeleted(NonReturn method) {
        this.onDeleted = method;
    }

    /**
     * Called when file has been created
     * I think it shouldn't be called
     *
     * @param method on created
     */
    public void onCreated(NonReturn method) {
        this.onCreated = method;
    }

    /**
     * Stop listening and auto reload
     */
    public void stop() {
        try {
            if (watchService != null) {
                watchService.close();
            }
            if (task != null) {
                task.cancel();
            }
        } catch (IOException ignored) {
            if (task != null) {
                task.cancel();
            }
        }
    }

    private void init(Plugin plugin) {
        this.task = new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    WatchService watcher = FileSystems.getDefault().newWatchService();
                    AutoReloadFile.this.watchService = watcher;
                    Paths.get(file.getParent()).register(watcher,
                            new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE,
                                    StandardWatchEventKinds.ENTRY_MODIFY,
                                    StandardWatchEventKinds.ENTRY_DELETE},
                            SensitivityWatchEventModifier.HIGH);
                    while (true) {
                        if (!plugin.isEnabled()) {
                            watcher.close();
                            cancel();
                            break;
                        }
                        WatchKey key = watcher.poll();
                        if (key == null) continue;
                        for (WatchEvent<?> event : key.pollEvents()) {
                            WatchEvent.Kind<?> kind = event.kind();
                            if (kind == StandardWatchEventKinds.OVERFLOW) {
                                continue;
                            }
                            Path path = (Path) event.context();
                            if (!file.getName().equals(path.toString())) {
                                continue;
                            }
                            if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        onCreated.apply();
                                    }
                                }.runTask(plugin);
                            } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        onDeleted.apply();
                                    }
                                }.runTask(plugin);
                            } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        onModified.apply();
                                    }
                                }.runTask(plugin);
                            }
                        }
                        key.reset();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

}
