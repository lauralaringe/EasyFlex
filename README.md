## Validating Flexibility Commitments with Hedera Hashgraph 

Hedera SDK used from https://github.com/hashgraph/hedera-sdk-java



## Requirements 
A .env file must be created with accounts and mirror node information.
To create an account go you need to create an Hedera Portal profile registeromg on https://portal.hedera.com/register.

Once you've completed setting up your profile, your home page will automatically create a testnet account and generate the public and private key pair associated with it. You can easily copy your accountId, public key, and private key information to your clipboard to use when configuring your SDK environment for testnet or set up the .env file. 

An .env file for this project should look like the text below.

```bash
# DSO account keys
OPERATOR_ID=0.0.1752
OPERATOR_KEY=302e020100300506032b65700422042068b0ecae635505d8t345674e4f7d8b5b52682e419068acd225
OPERATOR_PUBLIC_KEY=302a300506032b65700323457bb8c7b831d0d91d163b2ad791e75064f6015b2f5a621c2aea3eaec
MIRROR_NODE_ADDRESS=api.testnet.kabuto.sh:50211
NODE_ADDRESS=0.0.3

# User account keys 
OPERATOR_ID_2=0.0.2267
OPERATOR_KEY_2=302e020100300506032r535431100fb446efab9c0dbd3d2b1307ada346a769e6ebba0e45a48
OPERATOR_PUBLIC_KEY_2=302a300506032b6570032100d5dd7ea7ffbdab56670eefdd3168abe21c2c02a0cdac21de0e99adf44

```
## Roadmap
The idea is expanding the use of Hedera Hasgraph and creating a mainnet and using our own mirror node where to build a proper application to retrieve the flexibility resurces' power and energy information to display them and validate the flexibility commitment.

## Support
Laura Laringe: lauralaringe@gmail.com 

Oscar Damanik: oadamanik@gmail.com

## Authors
Ainhoa Sanchez, Emilia Chojkiewicz, Jorge Ballesteros, Hoda Ataee, Laura Laringe, Oscar Damanik
