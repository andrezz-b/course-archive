import { zodResolver } from "@hookform/resolvers/zod";
import { SubmitHandler, useForm } from "react-hook-form";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "../ui/form";
import { Input } from "../ui/input";
import { Button } from "../ui/button";
import {
  College,
  CollegeCreateData,
  CollegeCreateSchema,
  CollegeEditSchema,
} from "@/types/College";
import { useMemo } from "react";
import { CollegeService } from "@/api/college.service";
import { LoaderCircle } from "lucide-react";
import { ApiError } from "@/api/config/ApiError";

type CollegeAdminModalProps = {
  edit: boolean;
  college?: College;
  close: () => void;
};

const CollegeAdminModal = ({ edit, college, close }: CollegeAdminModalProps) => {
  const schema = useMemo(() => (edit ? CollegeEditSchema : CollegeCreateSchema), [edit]);
  const { mutateAsync: editCollege } = CollegeService.useUpdateCollegeById();
  const { mutateAsync: create } = CollegeService.useCreateCollege();
  const defaultValues = useMemo(
    () => ({
      name: college?.name ?? "",
      acronym: college?.acronym ?? "",
      city: college?.city ?? "",
      postcode: college?.postcode,
      address: college?.address ?? "",
      website: college?.website ?? "",
      description: college?.description ?? "",
    }),
    [college],
  );
  const form = useForm<CollegeCreateData>({
    defaultValues,
    resolver: zodResolver(schema),
  });

  const mutationOptions = useMemo(
    () => ({
      onSuccess: () => close(),
      onError: (error: ApiError) =>
        form.setError("root.serverError", {
          type: error.getStatusCode().toString(),
          message: error.getErrorMessage(),
        }),
    }),
    [form, close],
  );

  const onSubmit: SubmitHandler<CollegeCreateData> = async (data) => {
    const dirtyData = Object.keys(form.formState.dirtyFields).reduce((acc, key) => {
      if (key in form.formState.dirtyFields) {
        // @ts-expect-error Key is validated to be in dirtyFields
        acc[key] = data[key];
      }
      return acc;
    }, {} as CollegeCreateData);
    if (Object.keys(dirtyData).length === 0) {
      close();
      return;
    }
    try {
      if (edit && college) {
        await editCollege({ id: college.id, ...dirtyData }, mutationOptions);
      } else {
        await create(dirtyData, mutationOptions);
      }
    } catch (error) {
      // Handled by onError
    }
  };

  return (
    <>
      <h2 className="text-2xl font-bold">{edit ? `Edit: ${college?.name}` : "Create College"}</h2>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4" autoComplete="off">
          {!edit && (
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>College Name</FormLabel>
                  <FormControl>
                    <Input {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          )}
          <FormField
            control={form.control}
            name="acronym"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Acronym</FormLabel>
                <FormControl>
                  <Input {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="city"
            render={({ field }) => (
              <FormItem>
                <FormLabel>City</FormLabel>
                <FormControl>
                  <Input {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="postcode"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Postcode</FormLabel>
                <FormControl>
                  <Input type="number" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="address"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Address</FormLabel>
                <FormControl>
                  <Input {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="website"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Website</FormLabel>
                <FormControl>
                  <Input {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="description"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Description</FormLabel>
                <FormControl>
                  <Input {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          {form.formState.errors.root?.serverError.message && (
            <p className="text-destructive text-sm">
              {form.formState.errors.root?.serverError.message}
            </p>
          )}
          <div className="flex justify-between flex-row-reverse">
            <Button type="submit" disabled={form.formState.isSubmitting}>
              {form.formState.isSubmitting ? (
                <LoaderCircle className="animate-spin" />
              ) : edit ? (
                "Save Changes"
              ) : (
                "Create"
              )}
            </Button>
            {edit && (
              <Button variant="destructive" type="button" onClick={() => form.reset(defaultValues)}>
                Reset
              </Button>
            )}
          </div>
        </form>
      </Form>
    </>
  );
};

export default CollegeAdminModal;
