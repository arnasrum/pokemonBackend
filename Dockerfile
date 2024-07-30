#FROM vitess/lite:v20.0.0 

FROM node:22-alpine

WORKDIR /frontend

COPY ./frontend/package.json . 

COPY ./frontend/src ./ 

COPY ./frontend/public ./ 

EXPOSE 5173

RUN npm install

CMD ["npm", "run", "dev"]
