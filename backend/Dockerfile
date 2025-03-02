FROM php:8.2-cli

RUN apt-get update && apt-get install -y \
  git \
  libzip-dev \
  zip \
  unzip \
  && docker-php-ext-install pdo_mysql zip

RUN curl -sS https://getcomposer.org/installer | php -- --install-dir=/usr/local/bin --filename=composer

RUN composer global require laravel/installer

RUN echo 'export PATH="$HOME/.composer/vendor/bin:$PATH"' >> ~/.bashrc && . ~/.bashrc

WORKDIR /workspace/backend

COPY . .

RUN composer install --optimize-autoloader

CMD ["php", "artisan", "serve", "--host=0.0.0.0", "--port=8000"]