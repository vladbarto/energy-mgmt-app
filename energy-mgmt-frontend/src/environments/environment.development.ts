export const environment = {
  production: false,
  apiLoginServiceUrl: "http://localhost:6582/api-login/", // for local dev
                      //"http://localhost/api-login/", // for local docker
  userServicePreEndpoint: "user/v1",
  MONITORING_URL: "ws://localhost:6586/ws", // for local dev
          // 'ws://localhost/ws', // for local docker
  CHAT_URL: "ws://localhost:6587/wsChat" // for local dev
      // 'ws://localhost/wsChat' // for local docker
};
