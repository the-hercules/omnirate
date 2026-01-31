
import http from 'k6/http';
import { check } from 'k6';

// This configuration will simulate 50 virtual users for 30 seconds.
// You can adjust these values to increase or decrease the load.
export const options = {
  vus: 1,
  duration: '30s',
};

export default function () {
  // Generate a unique key for each virtual user and iteration to simulate different clients
  const uniqueKey = `user:${__VU}:${__ITER}`;

  const url = 'http://localhost:8080/rate-limit/check';
  const payload = JSON.stringify({
    key: uniqueKey,
    capacity: 10,
    refillRate: 1,
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  // Send the POST request
  const res = http.post(url, payload, params);

  // Check if the request was successful
  check(res, {
    'status is 200': (r) => r.status === 200,
  });
}
