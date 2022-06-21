package me.xstrixu.onlinegradebook.auth;

record ManagementCredentials(String username, String password) {

    boolean usernamePasswordEquals(String username, String password) {
        return username.equals(this.username) && password.equals(this.password);
    }
}
