FROM ubuntu:18.10
ADD backend /home/spotapp

# Add web files
ADD web /usr/share/nginx/html/
ADD web /var/www/html/

# Install dependencies
RUN apt-get update;
RUN apt-get install -y leiningen;
RUN apt-get install -y nginx nodejs;

# Remove default configuration file
RUN rm -v /etc/nginx/nginx.conf

# Add nginx configurations
ADD nginx.conf /etc/nginx/

# Append "daemon off;" to the beginning of the configuration
RUN echo "daemon on;" >> /etc/nginx/nginx.conf

# Expose ports
EXPOSE 90
EXPOSE 8000

WORKDIR "/home/spotapp/"

# Start nginx
CMD service nginx start && lein with-profile dev run
