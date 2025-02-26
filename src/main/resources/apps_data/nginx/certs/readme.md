This folder contains dummy certs. 

It is required because nginx can't start server with ssl configured but without cert files.

DockerCompose, which incorporates the certbot, will automatically generate live certificates, 
so these dummy certificates will be overridden.