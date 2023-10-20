CREATE TABLE IF NOT EXISTS assets(
  id UUID PRIMARY KEY NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  s3_key VARCHAR NOT NULL,
  public BOOLEAN NOT NULL,
  filename VARCHAR NULL
);