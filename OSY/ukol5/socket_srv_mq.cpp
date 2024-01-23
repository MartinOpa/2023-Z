//***************************************************************************
//
// Program example for labs in subject Operating Systems
//
// Petr Olivka, Dept. of Computer Science, petr.olivka@vsb.cz, 2017
//
// Example of socket server.
//
// This program is example of socket server and it allows to connect and serve
// the only one client.
// The mandatory argument of program is port number for listening.
//
//***************************************************************************

#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <stdarg.h>
#include <poll.h>
#include <sys/socket.h>
#include <sys/param.h>
#include <sys/time.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <errno.h>
#include <semaphore.h>
#include <pthread.h>
#include <queue>
#include <string>
#include <mqueue.h>

#define STR_CLOSE   "close"
#define STR_QUIT    "quit"

//***************************************************************************
// log messages

#define LOG_ERROR               0       // errors
#define LOG_INFO                1       // information and notifications
#define LOG_DEBUG               2       // debug messages

// debug flag
int g_debug = LOG_INFO;

using namespace std;

#define MQ_NAME "/mq_prodcon"

mqd_t prodcon;
int firstProc;

void log_msg( int t_log_level, const char *t_form, ... )
{
    const char *out_fmt[] = {
            "ERR: (%d-%s) %s\n",
            "INF: %s\n",
            "DEB: %s\n" };

    if ( t_log_level && t_log_level > g_debug ) return;

    char l_buf[ 1024 ];
    va_list l_arg;
    va_start( l_arg, t_form );
    vsprintf( l_buf, t_form, l_arg );
    va_end( l_arg );

    switch ( t_log_level )
    {
    case LOG_INFO:
    case LOG_DEBUG:
        fprintf( stdout, out_fmt[ t_log_level ], l_buf );
        break;

    case LOG_ERROR:
        fprintf( stderr, out_fmt[ t_log_level ], errno, strerror( errno ), l_buf );
        break;
    }
}

//***************************************************************************
// help

void help( int t_narg, char **t_args )
{
    if ( t_narg <= 1 || !strcmp( t_args[ 1 ], "-h" ) )
    {
        printf(
            "\n"
            "  Socket server example.\n"
            "\n"
            "  Use: %s [-h -d] port_number\n"
            "\n"
            "    -d  debug mode \n"
            "    -h  this help\n"
            "\n", t_args[ 0 ] );

        exit( 0 );
    }

    if ( !strcmp( t_args[ 1 ], "-d" ) )
        g_debug = LOG_DEBUG;
}

//***************************************************************************

void produce(string input) {
    char l_buf[64];
    memset(l_buf, 0, sizeof(l_buf));
    string msg = "TID: " + to_string(gettid()) + ", item: " + input;
    strncpy(l_buf, msg.c_str(), sizeof(l_buf));
    l_buf[sizeof(l_buf) - 1] = '\0';

    mq_send( prodcon, ( const char * ) &l_buf, sizeof( l_buf ), 0 );
}

string consume() {
    char l_buf[64];
 
    mq_receive( prodcon, ( char * ) &l_buf, sizeof( l_buf ), nullptr );
    
    string item(l_buf);
    item += "\n";

    return item;
}


void client(int fd) {
    int l_sock_client = fd;

    pollfd l_read_poll[ 2 ];
    l_read_poll[ 0 ].fd = STDIN_FILENO;
    l_read_poll[ 0 ].events = POLLIN;
    l_read_poll[ 1 ].fd = l_sock_client;
    l_read_poll[ 1 ].events = POLLIN;

    int vyroba = 0;
    int spotreba = 0;
    int firstConsume = 1;
    
    while ( 1  )
    { // communication
        if (firstConsume && spotreba) {
            string item = consume();         
            firstConsume = 0;
            write( l_sock_client, item.c_str(), strlen(item.c_str()) );
        }

        char l_buf[ 256 ];

        // select from fds
        int l_poll = poll( l_read_poll, 2, -1 );

        if ( l_poll < 0 )
        {
            log_msg( LOG_ERROR, "Function poll failed!" );
            exit( 1 );
        }
        // data from client?
        if ( l_read_poll[ 1 ].revents & POLLIN )
        {
            // read data from socket
            int l_len = read( l_sock_client, l_buf, sizeof( l_buf ) );

            /*if (spotreba) {
                l_len = write( STDOUT_FILENO, l_buf, l_len );
                if ( l_len < 0 )
                    log_msg( LOG_ERROR, "Unable to write to stdout." );
            }*/
                
            if ( !l_len )
            {
                log_msg( LOG_DEBUG, "Client closed socket!" );
                close( l_sock_client );
                exit(0);
            }
            else if ( l_len < 0 )
            {
                log_msg( LOG_ERROR, "Unable to read data from client." );
                close( l_sock_client );
                exit(1);
            }
            else
                log_msg( LOG_DEBUG, "Read %d bytes from client.", l_len );

            if (vyroba && l_len > 0) {
                produce(string(l_buf, l_len));
                write( l_sock_client, "ACK\n", strlen("ACK\n") );
            }

            if (spotreba && l_len > 0 && !strncasecmp(l_buf, "ACK", strlen("ACK"))) {                      
                string item = consume();   
                write( l_sock_client, item.c_str(), strlen(item.c_str()) );
                write(STDOUT_FILENO, "ACK\n", strlen("ACK\n"));
            }
            
            if (!vyroba && !spotreba && !strncasecmp( l_buf, "VYROBA", strlen( "VYROBA" ))) {
                vyroba = 1;         
                firstConsume = 0;
            }

            if (!vyroba && !spotreba && !strncasecmp( l_buf, "SPOTREBA", strlen( "SPOTREBA" ))) {
                spotreba = 1;
            }

            // close request?
            if ( !strncasecmp( l_buf, "close", strlen( STR_CLOSE ) ) )
            {
                log_msg( LOG_INFO, "Client sent 'close' request to close connection." );
                close( l_sock_client );
                log_msg( LOG_INFO, "Connection closed. Waiting for new client." );
                exit(0);
            }
        }
        // request for quit
        if ( !strncasecmp( l_buf, "quit", strlen( STR_QUIT ) ) )
        {
            close( l_sock_client );
            log_msg( LOG_INFO, "Request to 'quit' entered" );
            exit( 0 );
        }
    } // while communication 
}

void clean( void )
{
    log_msg( LOG_INFO, "Final cleaning ..." );

    if ( prodcon < 0 ) return;
    if (firstProc) {
        log_msg( LOG_INFO, "This process was first and now it will try remove queue ..." );

        if ( mq_unlink( MQ_NAME ) < 0 )
            log_msg( LOG_ERROR, "Unable to delete message queue!" );
        else
            log_msg( LOG_INFO, "Message queue deleted." );
    }
    
}

// catch signals
void catch_sig( int sig )
{
    exit( 1 );
}

int main( int t_narg, char **t_args )
{
    firstProc = 1;

    prodcon = mq_open(MQ_NAME, O_RDWR);
    if (prodcon < 0) {
        mq_attr l_mqa;
        bzero( &l_mqa, sizeof( l_mqa ) );
        l_mqa.mq_flags = 0;
        l_mqa.mq_maxmsg = 8;
        l_mqa.mq_msgsize = sizeof(char[64]);

        prodcon = mq_open(MQ_NAME, O_RDWR | O_CREAT, 0660, &l_mqa);
        if (prodcon < 0) {
            return 1;
        }
    }

    struct sigaction l_sa;
    bzero( &l_sa, sizeof( l_sa ) );
    l_sa.sa_handler = catch_sig;
    sigemptyset( &l_sa.sa_mask );
    l_sa.sa_flags = 0;

    // catch sig <CTRL-C>
    sigaction( SIGINT, &l_sa, nullptr );
    // catch SIG_PIPE
    sigaction( SIGPIPE, &l_sa, nullptr );

    // clean at exit
    atexit(clean);

    if ( t_narg <= 1 ) help( t_narg, t_args );

    int l_port = 0;

    // parsing arguments
    for ( int i = 1; i < t_narg; i++ )
    {
        if ( !strcmp( t_args[ i ], "-d" ) )
            g_debug = LOG_DEBUG;

        if ( !strcmp( t_args[ i ], "-h" ) )
            help( t_narg, t_args );

        if ( *t_args[ i ] != '-' && !l_port )
        {
            l_port = atoi( t_args[ i ] );
            break;
        }
    }

    if ( l_port <= 0 )
    {
        log_msg( LOG_INFO, "Bad or missing port number %d!", l_port );
        help( t_narg, t_args );
    }

    log_msg( LOG_INFO, "Server will listen on port: %d.", l_port );

    // socket creation
    int l_sock_listen = socket( AF_INET, SOCK_STREAM, 0 );
    if ( l_sock_listen == -1 )
    {
        log_msg( LOG_ERROR, "Unable to create socket.");
        exit( 1 );
    }

    in_addr l_addr_any = { INADDR_ANY };
    sockaddr_in l_srv_addr;
    l_srv_addr.sin_family = AF_INET;
    l_srv_addr.sin_port = htons( l_port );
    l_srv_addr.sin_addr = l_addr_any;

    // Enable the port number reusing
    int l_opt = 1;
    if ( setsockopt( l_sock_listen, SOL_SOCKET, SO_REUSEADDR, &l_opt, sizeof( l_opt ) ) < 0 )
      log_msg( LOG_ERROR, "Unable to set socket option!" );

    // assign port number to socket
    if ( bind( l_sock_listen, (const sockaddr * ) &l_srv_addr, sizeof( l_srv_addr ) ) < 0 )
    {
        log_msg( LOG_ERROR, "Bind failed!" );
        close( l_sock_listen );
        exit( 1 );
    }

    // listenig on set port
    if ( listen( l_sock_listen, 1 ) < 0 )
    {
        log_msg( LOG_ERROR, "Unable to listen on given port!" );
        close( l_sock_listen );
        exit( 1 );
    }

    log_msg( LOG_INFO, "Enter 'quit' to quit server." );

    // go!
    while ( 1 )
    {
        int l_sock_client = -1;

        // list of fd sources
        pollfd l_read_poll[ 2 ];

        l_read_poll[ 0 ].fd = STDIN_FILENO;
        l_read_poll[ 0 ].events = POLLIN;
        l_read_poll[ 1 ].fd = l_sock_listen;
        l_read_poll[ 1 ].events = POLLIN;

            // select from fds
            int l_poll = poll( l_read_poll, 2, -1 );

            if ( l_poll < 0 )
            {
                log_msg( LOG_ERROR, "Function poll failed!" );
                exit( 1 );
            }

            if ( l_read_poll[ 0 ].revents & POLLIN )
            { // data on stdin
                char buf[ 128 ];
                int len = read( STDIN_FILENO, buf, sizeof( buf) );
                if ( len < 0 )
                {
                    log_msg( LOG_DEBUG, "Unable to read from stdin!" );
                    exit( 1 );
                }

                log_msg( LOG_DEBUG, "Read %d bytes from stdin" );
                // request to quit?
                if ( !strncmp( buf, STR_QUIT, strlen( STR_QUIT ) ) )
                {
                    log_msg( LOG_INFO, "Request to 'quit' entered.");
                    close( l_sock_listen );
                    exit( 0 );
                }
            }

            if ( l_read_poll[ 1 ].revents & POLLIN )
            { // new client?
                sockaddr_in l_rsa;
                int l_rsa_size = sizeof( l_rsa );
                // new connection
                l_sock_client = accept( l_sock_listen, ( sockaddr * ) &l_rsa, ( socklen_t * ) &l_rsa_size );
                if ( l_sock_client == -1 )
                {
                    log_msg( LOG_ERROR, "Unable to accept new client." );
                    close( l_sock_listen );
                    exit( 1 );
                }
                uint l_lsa = sizeof( l_srv_addr );
                // my IP
                getsockname( l_sock_client, ( sockaddr * ) &l_srv_addr, &l_lsa );
                log_msg( LOG_INFO, "My IP: '%s'  port: %d",
                                 inet_ntoa( l_srv_addr.sin_addr ), ntohs( l_srv_addr.sin_port ) );
                // client IP
                getpeername( l_sock_client, ( sockaddr * ) &l_srv_addr, &l_lsa );
                log_msg( LOG_INFO, "Client IP: '%s'  port: %d",
                                 inet_ntoa( l_srv_addr.sin_addr ), ntohs( l_srv_addr.sin_port ) );

                if (fork() == 0) {
                    firstProc = 0;
                    client(l_sock_client);
                } else {
                    continue;
                }
        } // while wait for client
    }
    return 0;
}