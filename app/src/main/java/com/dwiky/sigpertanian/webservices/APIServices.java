package com.dwiky.sigpertanian.webservices;

import com.dwiky.sigpertanian.models.Agriculture;
import com.dwiky.sigpertanian.models.Comoditas;
import com.dwiky.sigpertanian.models.District;
import com.dwiky.sigpertanian.models.SubDistrict;
import com.dwiky.sigpertanian.responses.WrappedListResponse;
import com.dwiky.sigpertanian.responses.WrappedResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIServices{
    // Komoditas
    @GET("KomoditasLahan")
    Call<WrappedListResponse<Comoditas>> fetchComodity();

    @FormUrlEncoded
    @POST("KomoditasLahan")
    Call<WrappedResponse<Comoditas>> createComodity(
            @Field("namakomoditas") final String namakomoditas,
            @Field("jumlah") final String jumlah,
            @Field("awal") final String awal,
            @Field("akhir") final String akhir,
            @Field("desa") final String desa,
            @Field("kecamatan") final String kecamatan
    );

    @FormUrlEncoded
    @PUT("KomoditasLahan/{id}")
    Call<WrappedResponse<Comoditas>> updateComodity(
            @Path("id") final String id,
            @Field("namakomoditas") final String namakomoditas,
            @Field("jumlah") final String jumlah,
            @Field("awal") final String awal,
            @Field("akhir") final String akhir,
            @Field("desa") final String desa,
            @Field("kecamatan") final String kecamatan
    );

    @DELETE("KomoditasLahan/{id}")
    Call<WrappedResponse<Comoditas>> deleteComodity(
            @Path("id") final String id
    );


    //Lahan Pertanian
    @GET("LahanPetani")
    Call<WrappedListResponse<Agriculture>> fetchAgriculture();

    @Multipart
    @POST("LahanPetani")
    Call<WrappedResponse<Agriculture>> createAgriculture(
            @Part("namapemilik") final RequestBody namapemilik,
            @Part("luas") final RequestBody luas,
            @Part("meter") final RequestBody meter,
            @Part("desa") final RequestBody desa,
            @Part("kecamatan") final RequestBody kecamatan,
            @Part("latitude") final RequestBody latitude,
            @Part("longitude") final RequestBody longitude,
            @Part final MultipartBody.Part foto
    );






    // Desa
    @GET("Desa")
    Call<WrappedListResponse<SubDistrict>> fetchSubDistrict();

    // Kecamatan
    @GET("Kecamatan")
    Call<WrappedListResponse<District>> fetchDistrict();
}
