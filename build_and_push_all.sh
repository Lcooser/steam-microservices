#!/bin/bash

# Parar se qualquer comando falhar
set -e

# Array com os nomes dos diretórios e das imagens
declare -a services=("bank" "comment" "email" "friendship" "game" "invoice" "likesdislikes" "order" "player" "user")

# Loop para construir os JARs e enviar as imagens Docker
for service in "${services[@]}"
do
  echo "Construindo o JAR para o serviço $service..."

  # Navegar para o diretório do serviço
  cd ./$service

  # Construir o JAR
  mvn clean package -DskipTests

  echo "Construindo a imagem lcooser/$service:latest..."

  # Construir a imagem
  docker build -t lcooser/$service:latest .

  # Enviar a imagem
  docker push lcooser/$service:latest

  # Voltar para o diretório anterior
  cd ..
done

echo "Todos os JARs foram construídos e todas as imagens foram enviadas."
