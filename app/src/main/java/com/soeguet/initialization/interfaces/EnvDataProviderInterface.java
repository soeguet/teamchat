package com.soeguet.initialization.interfaces;

/** Interface for retrieving environment data based on a given key. */
public interface EnvDataProviderInterface {

  /**
   * Retrieve environment data for a given key.
   *
   * @param key The key for which environment data needs to be retrieved.
   * @return The environment data associated with the provided key. If no data is found, null is
   *     returned.
   */
  String getEnvData(String key);
}
