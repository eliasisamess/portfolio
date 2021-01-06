import hashlib
import binascii
import sys

# You can add all kind of config values to this file, e.g. CSV separator and file path

# this dictionary contains users, which are allowed to add new high score values
# Key: user name, Value: password in hashed format
users = {
    "username": "password"  # IF YOU NEED TO USE THIS FILE, CHANGE THESE TO CORRECT VALUES!
}


def verify_password(user, provided_password):
    # use the global users variable
    global users
    # Verify a stored password against one provided by user
    if user not in users:
        return False

    stored_password = users[user]
    salt = stored_password[:64]
    stored_password = stored_password[64:]
    pwdhash = hashlib.pbkdf2_hmac('sha512',
                                  provided_password.encode('utf-8'),
                                  salt.encode('ascii'),
                                  100000)
    pwdhash = binascii.hexlify(pwdhash).decode('ascii')
    return pwdhash == stored_password


def main():
    if len(sys.argv) != 3:
        print("Invalid arguments!")
        sys.exit(3)

    user = sys.argv[1]
    password = sys.argv[2]
    isValid = verify_password(user, password)
    if isValid:
        print("Password is correct.")
    else:
        print("Incorrect username and/or password!")


if __name__ == '__main__':
    main()
