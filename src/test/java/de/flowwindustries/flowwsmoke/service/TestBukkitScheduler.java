package de.flowwindustries.flowwsmoke.service;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class TestBukkitScheduler implements BukkitScheduler {

    public static int TASK_COUNTER = 0;

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        return 0;
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task, long delay) {
        return 0;
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task) {
        return 0;
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task) {
        return 0;
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period) {
        return TASK_COUNTER++;
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, BukkitRunnable task, long delay, long period) {
        return 0;
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        return 0;
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task) {
        return 0;
    }

    @Override
    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period) {
        return 0;
    }

    @Override
    public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> task) {
        return null;
    }

    @Override
    public void cancelTask(int taskId) {
        TASK_COUNTER--;
    }

    @Override
    public void cancelTasks(Plugin plugin) {

    }

    @Override
    public boolean isCurrentlyRunning(int taskId) {
        return false;
    }

    @Override
    public boolean isQueued(int taskId) {
        return false;
    }

    @Override
    public List<BukkitWorker> getActiveWorkers() {
        return null;
    }

    @Override
    public List<BukkitTask> getPendingTasks() {
        return null;
    }

    @Override
    public BukkitTask runTask(Plugin plugin, Runnable task) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void runTask(Plugin plugin, Consumer<? super BukkitTask> task) throws IllegalArgumentException {

    }

    @Override
    public BukkitTask runTask(Plugin plugin, BukkitRunnable task) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, Runnable task) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void runTaskAsynchronously(Plugin plugin, Consumer<? super BukkitTask> task) throws IllegalArgumentException {

    }

    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, BukkitRunnable task) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, Runnable task, long delay) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void runTaskLater(Plugin plugin, Consumer<? super BukkitTask> task, long delay) throws IllegalArgumentException {

    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, BukkitRunnable task, long delay) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, Runnable task, long delay) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void runTaskLaterAsynchronously(Plugin plugin, Consumer<? super BukkitTask> task, long delay) throws IllegalArgumentException {

    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, BukkitRunnable task, long delay) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, Runnable task, long delay, long period) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void runTaskTimer(Plugin plugin, Consumer<? super BukkitTask> task, long delay, long period) throws IllegalArgumentException {

    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, BukkitRunnable task, long delay, long period) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, Runnable task, long delay, long period) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void runTaskTimerAsynchronously(Plugin plugin, Consumer<? super BukkitTask> task, long delay, long period) throws IllegalArgumentException {

    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, BukkitRunnable task, long delay, long period) throws IllegalArgumentException {
        return null;
    }
}
