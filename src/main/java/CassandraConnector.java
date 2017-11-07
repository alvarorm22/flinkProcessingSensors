import com.datastax.driver.core.*;

import java.io.Serializable;

import static java.lang.System.out;

/**
 * Class used for connecting to Cassandra database.
 */

public class CassandraConnector implements Serializable{

    /** Cassandra Cluster. */
    private Cluster cluster;

    /** Cassandra Session. */
    private static Session session;

    public CassandraConnector(final String node, final int port) {

        this.connect(node, port);

    }

    /**
     * Connect to Cassandra Cluster specified by provided node IP
     * address and port number.
     *
     * @param node Cluster node IP address.
     * @param port Port of cluster host.
     */

    private void connect(final String node, final int port) {

        this.cluster = Cluster.builder().addContactPoint(node).withQueryOptions(new QueryOptions()
                .setFetchSize(50000)).withPort(port).build();

        final Metadata metadata = cluster.getMetadata();
        out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        for (final Host host : metadata.getAllHosts()) {
            out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
                    host.getDatacenter(), host.getAddress(), host.getRack());
        }

        session = cluster.connect();
    }

    /**
     * Provide my Session.
     *
     * @return My session.
     */


    public static Session getSession() {

        return session;
    }

    /** Close cluster. */

    public void close() {

        cluster.close();

    }

}
