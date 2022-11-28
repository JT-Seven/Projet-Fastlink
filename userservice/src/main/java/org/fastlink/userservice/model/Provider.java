package org.fastlink.userservice.model;


public enum Provider
{

    LOCAL("local"), GOOGLE("google");

    private final String provider;

    Provider(String provider)
    {
        this.provider = provider;
    }

    public String getProvider()
    {
        return this.provider;
    }

}
