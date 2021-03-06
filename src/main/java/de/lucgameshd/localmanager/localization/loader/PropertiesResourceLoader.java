package de.lucgameshd.localmanager.localization.loader;

import de.lucgameshd.localmanager.localization.ResourceLoadFailedException;
import de.lucgameshd.localmanager.localization.ResourceLoader;
import de.lucgameshd.localmanager.localization.ResourceManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PropertiesResourceLoader extends FileResourceLoader implements ResourceLoader {

    private Properties pro;
    private String file;
    private ArrayList<String> keys = new ArrayList<>();

    /**
     * Empty Constructor template for the
     * {@link ResourceManager#registerLoader(ResourceLoader)}
     */
    public PropertiesResourceLoader() {

    }

    /**
     * Load a new PropertiesResource
     *
     * @param classLoader The classLoader for which this Resource should be loaded
     * @param file        The file to load
     * @throws ResourceLoadFailedException if the stream could not be closed
     */
    public PropertiesResourceLoader( ClassLoader classLoader, String file ) throws ResourceLoadFailedException {
        super( classLoader );

        this.file = file;

        try {
            load();
        } catch ( ResourceLoadFailedException e ) {
            throw e;
        }
    }

    private void load() throws ResourceLoadFailedException {
        InputStreamReader stream = null;
        try {
            //Get the correct InputStreamReader for this file
            stream = getFileInputStreamReader( file );

            //Try to parse the properties
            pro = new Properties();
            pro.load( stream );

            //Get the keys
            keys = new ArrayList<>();

            for ( Object o : pro.keySet() ) {
                keys.add( (String) o );
            }
        } catch ( IOException e ) {
            pro = null;
            throw new ResourceLoadFailedException( e );
        } catch ( ResourceLoadFailedException e ) {
            throw e;
        } finally {
            if ( stream != null ) {
                try {
                    stream.close();
                } catch ( IOException e ) {
                    throw new ResourceLoadFailedException( e );
                }
            }
        }
    }

    /**
     * Get all keys which can be handled by this Resource
     *
     * @return List of keys available
     */
    @Override
    public List<String> getKeys() {
        return keys;
    }

    /**
     * Get the key from the Properties
     *
     * @param key Key to get
     * @return The object from Properties or null if Properties loading was an error
     */
    @Override
    public String get( String key ) {
        return pro != null ? (String) pro.get( key ) : null;
    }

    /**
     * Get the Formats this Loader can load
     *
     * @return A List of String as formats this Loader supports
     */
    @Override
    public List<String> getFormats() {
        return Arrays.asList( ".properties" );
    }

    /**
     * Force the reload of this Resource
     *
     * @throws ResourceLoadFailedException
     */
    @Override
    public void reload() throws ResourceLoadFailedException {
        try {
            load();
        } catch ( ResourceLoadFailedException e ) {
            throw e;
        }
    }

    /**
     * If plugin gets unloaded remove all refs
     */
    @Override
    public void cleanup() {
        pro = null;
        file = null;

        super.cleanup();
    }

}