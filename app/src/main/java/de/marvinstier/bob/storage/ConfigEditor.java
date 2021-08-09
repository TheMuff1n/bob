package de.marvinstier.bob.storage;

public abstract class ConfigEditor {
    /**
     * This method should implement a way to store data belonging to a key and
     * server. It should be able to be retrieved later by providing a server id and
     * the key.
     * 
     * @param serverId guild id
     * @param key string to find the data at
     * @param data string representing the data
     */
    public abstract void save(long serverId, String key, String data);

    /**
     * This method should implement a way to retrieve data by providing a 
     * @param serverId guild id
     * @param key string to look for data at
     * @return data stored at given key for given server, null if not found
     */
    public abstract String load(long serverId, String key);
}
