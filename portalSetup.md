- Install Node JS in instance:
***sudo yum install -y gcc-c++ make
***curl -sL https://rpm.nodesource.com/setup_10.x | sudo -E bash -
***sudo yum install -y nodejs

- Install PM2 on the instance
***sudo npm i -g pm2

- Copy deployment folder to the instance by WInSCP and do:
***npm i

- Navigate inside deployment folder and run:
***sudo pm2 start app.js
***sudo pm2 startup
***sudo pm2 save

- To view running instances:
***pm2 list  