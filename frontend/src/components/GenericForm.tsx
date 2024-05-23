import { zodResolver } from "@hookform/resolvers/zod";
import { SubmitHandler, useForm, Path, FieldValues, DefaultValues } from "react-hook-form";
import { ZodSchema } from "zod";
import { Button } from "./ui/button";
import { FormField, FormItem, FormLabel, FormControl, FormMessage, Form } from "./ui/form";
import { Input } from "./ui/input";
import { getZodSchemaFieldsShallow } from "@/lib/utils";
import { useCallback, useMemo } from "react";
import { LoaderCircle } from "lucide-react";

interface GenericFormProps<T extends FieldValues> {
  schema: ZodSchema<T>;
  defaultValues: DefaultValues<T>;
  onSubmit: (data: T) => Promise<{ type: string; message: string } | undefined>;
  title: string;
  closeDialog: () => void;
  showReset?: boolean;
}

const GenericForm = <T extends FieldValues>({
  schema,
  defaultValues,
  onSubmit,
  title,
  showReset = false,
  closeDialog,
}: GenericFormProps<T>) => {
  const form = useForm<T>({
    defaultValues,
    mode: "onTouched",
    resolver: zodResolver(schema),
  });

  const fields = useMemo(() => Object.keys(getZodSchemaFieldsShallow(schema)), [schema]);

  const firstLetterUpperCase = useCallback(
    (str: string) => str.charAt(0).toUpperCase() + str.slice(1),
    [],
  );

  const formSubmit: SubmitHandler<T> = async (data) => {
    const result = await onSubmit(data);
    if (result) {
      form.setError("root.serverError", {
        type: result.type,
        message: result.message,
      });
      return;
    }
    closeDialog();
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(formSubmit)} className="space-y-4" autoComplete="off">
        <h2 className="font-semibold">{title}</h2>
        {fields.map((field) => (
          <FormField
            key={String(field)}
            control={form.control}
            name={field as Path<T>}
            render={({ field: formField }) => (
              <FormItem>
                <FormLabel>{firstLetterUpperCase(String(field))}:</FormLabel>
                <FormControl>
                  <Input {...formField} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        ))}
        {form.formState.errors.root?.serverError.message && (
          <p className="text-destructive text-sm">
            {form.formState.errors.root?.serverError.message}
          </p>
        )}
        <div className="flex justify-between flex-row-reverse">
          <Button type="submit" disabled={form.formState.isSubmitting} className="w-[5rem]">
            {form.formState.isSubmitting ? <LoaderCircle className="animate-spin" /> : "Submit"}
          </Button>
          {showReset && (
            <Button variant="destructive" type="button" onClick={() => form.reset(defaultValues)}>
              Reset
            </Button>
          )}
        </div>
      </form>
    </Form>
  );
};

export default GenericForm;
